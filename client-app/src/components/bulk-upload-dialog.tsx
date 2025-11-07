import { useState, useRef } from 'react'
import { Upload, Download, FileSpreadsheet, AlertCircle, CheckCircle2, Loader2 } from 'lucide-react'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Progress } from '@/components/ui/progress'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { ScrollArea } from '@/components/ui/scroll-area'
import { cn } from '@/lib/utils'

export interface BulkUploadProgress {
  status: 'PROCESSING' | 'COMPLETED' | 'COMPLETED_WITH_ERRORS' | 'FAILED'
  totalRecords: number
  processedRecords: number
  successCount: number
  failureCount: number
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
}

interface BulkUploadDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  title: string
  description: string
  onUpload: (file: File, onProgress: (progress: BulkUploadProgress) => void) => Promise<void>
  onExport: () => Promise<Blob>
  onDownloadTemplate: () => Promise<Blob>
  onSuccess?: () => void
}

export function BulkUploadDialog({
  open,
  onOpenChange,
  title,
  description,
  onUpload,
  onExport,
  onDownloadTemplate,
  onSuccess,
}: BulkUploadDialogProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null)
  const [uploading, setUploading] = useState(false)
  const [progress, setProgress] = useState<BulkUploadProgress | null>(null)
  const [dragActive, setDragActive] = useState(false)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true)
    } else if (e.type === 'dragleave') {
      setDragActive(false)
    }
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      const file = e.dataTransfer.files[0]
      if (file.name.endsWith('.xlsx')) {
        setSelectedFile(file)
      }
    }
  }

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setSelectedFile(e.target.files[0])
    }
  }

  const handleUpload = async () => {
    if (!selectedFile) return

    setUploading(true)
    setProgress(null)

    try {
      await onUpload(selectedFile, (prog) => {
        setProgress(prog)
      })

      if (onSuccess) {
        onSuccess()
      }
    } catch (error) {
      setProgress({
        status: 'FAILED',
        totalRecords: 0,
        processedRecords: 0,
        successCount: 0,
        failureCount: 0,
        progressPercentage: 0,
        message: error instanceof Error ? error.message : 'Upload failed',
        timestamp: new Date().toISOString(),
      })
    } finally {
      setUploading(false)
    }
  }

  const handleExport = async () => {
    try {
      const blob = await onExport()
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `${title.replace(/\s+/g, '_')}_Export_${new Date().toISOString().split('T')[0]}.xlsx`
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
    } catch (error) {
      // Handle error
    }
  }

  const handleDownloadTemplate = async () => {
    try {
      const blob = await onDownloadTemplate()
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `${title.replace(/\s+/g, '_')}_Template.xlsx`
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
    } catch (error) {
      // Handle error
    }
  }

  const handleClose = () => {
    if (!uploading) {
      setSelectedFile(null)
      setProgress(null)
      onOpenChange(false)
    }
  }

  const isCompleted =
    progress?.status === 'COMPLETED' || progress?.status === 'COMPLETED_WITH_ERRORS'
  const isFailed = progress?.status === 'FAILED'

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className='max-w-2xl max-h-[90vh] flex flex-col'>
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
          <DialogDescription>{description}</DialogDescription>
        </DialogHeader>

        <div className='flex-1 space-y-4 overflow-hidden'>
          {/* Template and Export Buttons */}
          <div className='flex gap-2'>
            <Button
              variant='outline'
              size='sm'
              onClick={handleDownloadTemplate}
              disabled={uploading}
              className='flex-1'
            >
              <FileSpreadsheet className='mr-2 h-4 w-4' />
              Download Template
            </Button>
            <Button
              variant='outline'
              size='sm'
              onClick={handleExport}
              disabled={uploading}
              className='flex-1'
            >
              <Download className='mr-2 h-4 w-4' />
              Export Current Data
            </Button>
          </div>

          {/* File Upload Area */}
          {!progress && (
            <div
              className={cn(
                'border-2 border-dashed rounded-lg p-8 text-center transition-colors',
                dragActive
                  ? 'border-primary bg-primary/5'
                  : 'border-muted-foreground/25 hover:border-muted-foreground/50',
                uploading && 'opacity-50 pointer-events-none'
              )}
              onDragEnter={handleDrag}
              onDragLeave={handleDrag}
              onDragOver={handleDrag}
              onDrop={handleDrop}
            >
              <Upload className='mx-auto h-12 w-12 text-muted-foreground mb-4' />
              <div className='space-y-2'>
                <p className='text-sm font-medium'>
                  {selectedFile ? selectedFile.name : 'Drop your Excel file here'}
                </p>
                <p className='text-xs text-muted-foreground'>
                  or click to browse (.xlsx files only)
                </p>
              </div>
              <input
                ref={fileInputRef}
                type='file'
                accept='.xlsx'
                onChange={handleFileSelect}
                className='hidden'
              />
              <Button
                variant='outline'
                size='sm'
                className='mt-4'
                onClick={() => fileInputRef.current?.click()}
                disabled={uploading}
              >
                Browse Files
              </Button>
            </div>
          )}

          {/* Progress */}
          {progress && (
            <div className='space-y-4'>
              <div className='space-y-2'>
                <div className='flex items-center justify-between text-sm'>
                  <span className='font-medium'>{progress.message}</span>
                  <span className='text-muted-foreground'>
                    {progress.processedRecords}/{progress.totalRecords}
                  </span>
                </div>
                <Progress value={progress.progressPercentage} className='h-2' />
                <div className='flex justify-between text-xs text-muted-foreground'>
                  <span>Success: {progress.successCount}</span>
                  <span>Failed: {progress.failureCount}</span>
                </div>
              </div>

              {/* Status Alert */}
              {isCompleted && (
                <Alert className={progress.failureCount > 0 ? 'border-yellow-500' : 'border-green-500'}>
                  {progress.failureCount > 0 ? (
                    <AlertCircle className='h-4 w-4 text-yellow-500' />
                  ) : (
                    <CheckCircle2 className='h-4 w-4 text-green-500' />
                  )}
                  <AlertDescription>
                    Upload completed: {progress.successCount} succeeded, {progress.failureCount}{' '}
                    failed
                  </AlertDescription>
                </Alert>
              )}

              {isFailed && (
                <Alert variant='destructive'>
                  <AlertCircle className='h-4 w-4' />
                  <AlertDescription>{progress.message}</AlertDescription>
                </Alert>
              )}

              {/* Errors */}
              {progress.errors && progress.errors.length > 0 && (
                <div className='space-y-2'>
                  <h4 className='text-sm font-medium'>Errors ({progress.errors.length}):</h4>
                  <ScrollArea className='h-48 rounded-md border p-4'>
                    <div className='space-y-2'>
                      {progress.errors.map((error, index) => (
                        <div key={index} className='text-xs space-y-1 pb-2 border-b last:border-0'>
                          <div className='font-medium'>Row {error.rowNumber}</div>
                          <div className='text-muted-foreground'>
                            {error.fieldName}: {error.errorMessage}
                          </div>
                          {error.rejectedValue && (
                            <div className='text-muted-foreground'>
                              Value: {error.rejectedValue}
                            </div>
                          )}
                        </div>
                      ))}
                    </div>
                  </ScrollArea>
                </div>
              )}
            </div>
          )}
        </div>

        <DialogFooter>
          {!progress && (
            <>
              <Button variant='outline' onClick={handleClose} disabled={uploading}>
                Cancel
              </Button>
              <Button onClick={handleUpload} disabled={!selectedFile || uploading}>
                {uploading && <Loader2 className='mr-2 h-4 w-4 animate-spin' />}
                Upload
              </Button>
            </>
          )}
          {progress && (
            <Button onClick={handleClose} disabled={uploading}>
              {uploading ? 'Uploading...' : 'Close'}
            </Button>
          )}
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
