import { useState, useRef } from 'react'
import { Upload, Download, FileSpreadsheet, X, AlertCircle, CheckCircle2, Loader2 } from 'lucide-react'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Progress } from '@/components/ui/progress'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { ScrollArea } from '@/components/ui/scroll-area'
import { statesApi, type BulkUploadProgress, type BulkUploadError } from '../api/states-api'
import { toast } from 'sonner'

interface BulkUploadDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onSuccess?: () => void
}

export function BulkUploadDialog({ open, onOpenChange, onSuccess }: BulkUploadDialogProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null)
  const [isUploading, setIsUploading] = useState(false)
  const [progress, setProgress] = useState<BulkUploadProgress | null>(null)
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false)
  const [isExporting, setIsExporting] = useState(false)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (file) {
      if (!file.name.endsWith('.xlsx')) {
        toast.error('Invalid file type', {
          description: 'Please select an Excel file (.xlsx)',
        })
        return
      }
      setSelectedFile(file)
      setProgress(null)
    }
  }

  const handleRemoveFile = () => {
    setSelectedFile(null)
    setProgress(null)
    if (fileInputRef.current) {
      fileInputRef.current.value = ''
    }
  }

  const handleUpload = async () => {
    if (!selectedFile) return

    setIsUploading(true)
    setProgress(null)

    try {
      await statesApi.bulkUpload(selectedFile, (progressData) => {
        setProgress(progressData)
        
        // When upload completes, show appropriate toast
        if (progressData.status === 'COMPLETED') {
          toast.success('Upload completed successfully!', {
            description: `Successfully uploaded ${progressData.successCount} records`,
          })
          
          if (onSuccess) {
            onSuccess()
          }
          
          // Reset and close after success
          setTimeout(() => {
            handleRemoveFile()
            setProgress(null)
            onOpenChange(false)
          }, 2000)
        } else if (progressData.status === 'COMPLETED_WITH_ERRORS') {
          toast.warning('Upload completed with errors', {
            description: `Success: ${progressData.successCount}, Failed: ${progressData.failureCount}. Please review the errors below.`,
          })
          
          if (onSuccess) {
            onSuccess()
          }
          
          // Don't close the dialog - user needs to see errors
        } else if (progressData.status === 'FAILED') {
          toast.error('Upload failed', {
            description: progressData.message || 'An error occurred during upload',
          })
        }
      })
    } catch (error) {
      toast.error('Upload failed', {
        description: error instanceof Error ? error.message : 'An error occurred during upload',
      })
      setProgress(null)
    } finally {
      setIsUploading(false)
    }
  }

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true)
    try {
      const blob = await statesApi.downloadTemplate()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = 'State_Upload_Template.xlsx'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)

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
    setIsExporting(true)
    try {
      const blob = await statesApi.exportToExcel()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      link.download = `States_Export_${timestamp}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)

      toast.success('States exported successfully')
    } catch (error) {
      toast.error('Failed to export states', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsExporting(false)
    }
  }

  const getStatusIcon = () => {
    if (!progress) return null
    
    switch (progress.status) {
      case 'PROCESSING':
        return <Loader2 className="h-5 w-5 animate-spin text-blue-500" />
      case 'COMPLETED':
        return <CheckCircle2 className="h-5 w-5 text-green-500" />
      case 'COMPLETED_WITH_ERRORS':
        return <AlertCircle className="h-5 w-5 text-yellow-500" />
      case 'FAILED':
        return <AlertCircle className="h-5 w-5 text-red-500" />
      default:
        return null
    }
  }

  const getStatusColor = () => {
    if (!progress) return 'bg-blue-500'
    
    switch (progress.status) {
      case 'PROCESSING':
        return 'bg-blue-500'
      case 'COMPLETED':
        return 'bg-green-500'
      case 'COMPLETED_WITH_ERRORS':
        return 'bg-yellow-500'
      case 'FAILED':
        return 'bg-red-500'
      default:
        return 'bg-blue-500'
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle>Bulk Upload States</DialogTitle>
          <DialogDescription>
            Upload multiple states at once using an Excel file
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          {/* Action Buttons */}
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={handleDownloadTemplate}
              disabled={isDownloadingTemplate || isUploading}
            >
              {isDownloadingTemplate ? (
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              ) : (
                <Download className="mr-2 h-4 w-4" />
              )}
              Download Template
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={handleExport}
              disabled={isExporting || isUploading}
            >
              {isExporting ? (
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              ) : (
                <FileSpreadsheet className="mr-2 h-4 w-4" />
              )}
              Export Current States
            </Button>
          </div>

          {/* File Upload Area */}
          <div className="border-2 border-dashed rounded-lg p-8">
            <input
              ref={fileInputRef}
              type="file"
              accept=".xlsx"
              onChange={handleFileSelect}
              className="hidden"
              disabled={isUploading}
            />

            {!selectedFile ? (
              <div className="text-center">
                <Upload className="mx-auto h-12 w-12 text-muted-foreground" />
                <div className="mt-4">
                  <Button
                    variant="secondary"
                    onClick={() => fileInputRef.current?.click()}
                    disabled={isUploading}
                  >
                    Select Excel File
                  </Button>
                </div>
                <p className="mt-2 text-sm text-muted-foreground">
                  Only .xlsx files are supported
                </p>
              </div>
            ) : (
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <FileSpreadsheet className="h-8 w-8 text-green-500" />
                  <div>
                    <p className="font-medium">{selectedFile.name}</p>
                    <p className="text-sm text-muted-foreground">
                      {(selectedFile.size / 1024).toFixed(2)} KB
                    </p>
                  </div>
                </div>
                {!isUploading && (
                  <Button
                    variant="ghost"
                    size="icon"
                    onClick={handleRemoveFile}
                  >
                    <X className="h-4 w-4" />
                  </Button>
                )}
              </div>
            )}
          </div>

          {/* Progress Section */}
          {progress && (
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  {getStatusIcon()}
                  <span className="text-sm font-medium">{progress.message}</span>
                </div>
                <span className="text-sm text-muted-foreground">
                  {progress.progressPercentage.toFixed(0)}%
                </span>
              </div>

              <Progress 
                value={progress.progressPercentage} 
                className={getStatusColor()}
              />

              <div className="grid grid-cols-3 gap-4 text-sm">
                <div className="text-center p-2 bg-muted rounded">
                  <p className="font-medium">{progress.totalRecords}</p>
                  <p className="text-muted-foreground">Total</p>
                </div>
                <div className="text-center p-2 bg-green-50 dark:bg-green-950 rounded">
                  <p className="font-medium text-green-700 dark:text-green-400">
                    {progress.successCount}
                  </p>
                  <p className="text-muted-foreground">Success</p>
                </div>
                <div className="text-center p-2 bg-red-50 dark:bg-red-950 rounded">
                  <p className="font-medium text-red-700 dark:text-red-400">
                    {progress.failureCount}
                  </p>
                  <p className="text-muted-foreground">Failed</p>
                </div>
              </div>

              {/* Errors Section */}
              {progress.errors && progress.errors.length > 0 && (
                <Alert variant="destructive">
                  <AlertCircle className="h-4 w-4" />
                  <AlertDescription>
                    <div className="font-medium mb-2">
                      {progress.errors.length} error(s) found:
                    </div>
                    <ScrollArea className="h-32 w-full rounded border p-2">
                      <div className="space-y-2">
                        {progress.errors.map((error: BulkUploadError, index: number) => (
                          <div key={index} className="text-sm">
                            <span className="font-medium">Row {error.rowNumber}:</span>{' '}
                            {error.fieldName && (
                              <span className="text-muted-foreground">
                                [{error.fieldName}]
                              </span>
                            )}{' '}
                            {error.errorMessage}
                            {error.rejectedValue && (
                              <span className="text-muted-foreground">
                                {' '}(value: "{error.rejectedValue}")
                              </span>
                            )}
                          </div>
                        ))}
                      </div>
                    </ScrollArea>
                  </AlertDescription>
                </Alert>
              )}
            </div>
          )}

          {/* Upload Button */}
          <div className="flex justify-end gap-2">
            <Button
              variant="outline"
              onClick={() => onOpenChange(false)}
              disabled={isUploading}
            >
              Cancel
            </Button>
            <Button
              onClick={handleUpload}
              disabled={!selectedFile || isUploading}
            >
              {isUploading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Uploading...
                </>
              ) : (
                <>
                  <Upload className="mr-2 h-4 w-4" />
                  Upload
                </>
              )}
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}
