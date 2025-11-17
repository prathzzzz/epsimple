import { useState, useEffect, useCallback } from 'react'
import { toast } from 'sonner'
import { downloadFileWithPost, downloadFile, createAuthHeaders } from '@/lib/api-utils'

export interface BulkUploadProgress {
  status: 'PROCESSING' | 'COMPLETED' | 'COMPLETED_WITH_ERRORS' | 'FAILED'
  totalRecords: number
  processedRecords: number
  successCount: number
  failureCount: number
  duplicateCount: number
  skippedCount: number
  progressPercentage: number
  message: string
  errors?: BulkUploadError[]
  timestamp: string
}

export interface BulkUploadError {
  rowNumber: number
  fieldName: string
  errorMessage: string
  rejectedValue?: string
  errorType?: 'VALIDATION' | 'DUPLICATE' | 'ERROR'
  rowData?: Record<string, unknown>
}

export interface BulkUploadConfig {
  entityName: string // "State", "City", etc.
  uploadEndpoint: string // "/api/states/bulk-upload"
  templateEndpoint?: string // "/api/states/download-template"
  exportEndpoint?: string // "/api/states/export"
  errorReportEndpoint: string // "/api/states/export-errors"
  onSuccess?: () => void
}

export function useBulkUpload(config: BulkUploadConfig) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null)
  const [isUploading, setIsUploading] = useState(false)
  const [progress, setProgress] = useState<BulkUploadProgress | null>(null)
  const [errorReportDownloaded, setErrorReportDownloaded] = useState(false)
  const [abortController, setAbortController] = useState<AbortController | null>(null)
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false)
  const [isExporting, setIsExporting] = useState(false)

  const handleDownloadErrorReport = useCallback(async () => {
    if (!progress?.errors?.length) return

    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      await downloadFileWithPost(
        config.errorReportEndpoint,
        progress as unknown as Record<string, unknown>,
        `${config.entityName}_Upload_Errors_${timestamp}.xlsx`
      )
      toast.success('Error report downloaded')
    } catch (error) {
      toast.error('Failed to download error report', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    }
  }, [progress, config.errorReportEndpoint, config.entityName])

  // Auto-download error report when errors are present
  useEffect(() => {
    if (
      progress &&
      progress.errors &&
      progress.errors.length > 0 &&
      !errorReportDownloaded &&
      (progress.status === 'COMPLETED_WITH_ERRORS' || progress.status === 'FAILED')
    ) {
      handleDownloadErrorReport()
      setErrorReportDownloaded(true)
    }
  }, [progress, errorReportDownloaded, handleDownloadErrorReport])

  const handleFileSelect = (file: File) => {
    if (!file.name.endsWith('.xlsx')) {
      toast.error('Invalid file type', {
        description: 'Please select an Excel file (.xlsx)',
      })
      return false
    }
    setSelectedFile(file)
    setProgress(null)
    setErrorReportDownloaded(false)
    return true
  }

  const handleRemoveFile = () => {
    setSelectedFile(null)
    setProgress(null)
    setErrorReportDownloaded(false)
  }

  const handleUpload = async () => {
    if (!selectedFile) return

    // Abort any existing upload
    if (abortController) {
      abortController.abort()
    }

    // Create new abort controller
    const controller = new AbortController()
    setAbortController(controller)

    setIsUploading(true)
    setProgress(null)
    setErrorReportDownloaded(false)

    try {
      await bulkUploadWithSSE(config.uploadEndpoint, selectedFile, controller.signal, (progressData) => {
        setProgress(progressData)

        if (progressData.status === 'COMPLETED') {
          setIsUploading(false)
          toast.success(`${config.entityName} upload completed!`, {
            description: `Successfully uploaded ${progressData.successCount} records`,
          })
          config.onSuccess?.()
        } else if (progressData.status === 'COMPLETED_WITH_ERRORS') {
          setIsUploading(false)
          toast.warning(`${config.entityName} upload completed with errors`, {
            description: `Success: ${progressData.successCount}, Failed: ${progressData.failureCount}, Duplicates: ${progressData.duplicateCount}`,
          })
          config.onSuccess?.()
        } else if (progressData.status === 'FAILED') {
          setIsUploading(false)
          toast.error(`${config.entityName} upload failed`, {
            description: progressData.message,
          })
        }
      })
    } catch (error) {
      if (error instanceof Error && error.name === 'AbortError') {
        return
      }
      toast.error('Upload failed', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
      setProgress(null)
    } finally {
      setIsUploading(false)
      setAbortController(null)
    }
  }

  const handleDownloadTemplate = async () => {
    if (!config.templateEndpoint) {
      toast.error('Template download not available')
      return
    }

    setIsDownloadingTemplate(true)
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      await downloadFile(
        config.templateEndpoint,
        `${config.entityName}_Upload_Template_${timestamp}.xlsx`
      )
      toast.success('Template downloaded successfully')
    } catch (error) {
      toast.error('Failed to download template', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsDownloadingTemplate(false)
    }
  }

  const handleExport = async () => {
    if (!config.exportEndpoint) {
      toast.error('Export not available')
      return
    }

    setIsExporting(true)
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      await downloadFile(
        config.exportEndpoint,
        `${config.entityName}s_Export_${timestamp}.xlsx`
      )
      toast.success(`${config.entityName}s exported successfully`)
    } catch (error) {
      toast.error(`Failed to export ${config.entityName}s`, {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsExporting(false)
    }
  }

  return {
    selectedFile,
    isUploading,
    progress,
    isDownloadingTemplate,
    isExporting,
    handleFileSelect,
    handleRemoveFile,
    handleUpload,
    handleDownloadTemplate,
    handleExport,
    handleDownloadErrorReport,
  }
}

// Helper function for SSE bulk upload
async function bulkUploadWithSSE(
  endpoint: string,
  file: File,
  signal: AbortSignal,
  onProgress: (progress: BulkUploadProgress) => void
): Promise<void> {
  const formData = new FormData()
  formData.append('file', file)

  const response = await fetch(`${import.meta.env.VITE_API_URL}${endpoint}`, {
    method: 'POST',
    body: formData,
    credentials: 'include',
    headers: createAuthHeaders(),
    signal, // Add abort signal
  })

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error('Authentication required. Please log in again.')
    }

    // Handle validation errors (400) before SSE stream starts
    if (response.status === 400) {
      try {
        const errorText = await response.text()
        const errorData = JSON.parse(errorText)
        throw new Error(errorData.message || 'Invalid file format or content')
      } catch (_jsonError) {
        throw new Error('Invalid file format or content')
      }
    }

    throw new Error(`Upload failed: ${response.statusText}`)
  }

  const reader = response.body?.getReader()
  const decoder = new TextDecoder()

  if (!reader) {
    throw new Error('Response body is not readable')
  }

  let buffer = '' // Buffer to accumulate chunks

  try {
    while (true) {
      const { done, value } = await reader.read()
      
      if (done) {
        break
      }

      // Decode the chunk and add to buffer
      const chunk = decoder.decode(value, { stream: true })
      buffer += chunk

      // Process complete SSE messages (ending with \n\n)
      const messages = buffer.split('\n\n')
      // Keep the last incomplete message in the buffer
      buffer = messages.pop() || ''

      for (const message of messages) {
        if (!message.trim()) continue

        // Parse SSE message format: event:progress\ndata:{json}
        const lines = message.split('\n')
        let data = ''
        
        for (const line of lines) {
          if (line.startsWith('data:')) {
            data = line.substring(5).trim()
          }
        }

        if (data) {
          try {
            const progress: BulkUploadProgress = JSON.parse(data)
            onProgress(progress)
            
            // Close the reader if we've reached a terminal status
            if (progress.status === 'COMPLETED' || progress.status === 'COMPLETED_WITH_ERRORS' || progress.status === 'FAILED') {
              return
            }
          } catch (_error) {
            // Ignore parsing errors for SSE data
          }
        }
      }
    }
  } finally {
    reader.releaseLock()
  }
}
