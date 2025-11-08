import { useRef } from 'react'
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
import { useBulkUpload, type BulkUploadConfig } from '@/hooks/useBulkUpload'

interface GenericBulkUploadDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  config: BulkUploadConfig
}

export function GenericBulkUploadDialog({ open, onOpenChange, config }: GenericBulkUploadDialogProps) {
  const fileInputRef = useRef<HTMLInputElement>(null)

  const {
    selectedFile,
    isUploading,
    progress,
    handleFileSelect,
    handleRemoveFile,
    handleUpload,
    handleDownloadErrorReport,
  } = useBulkUpload(config)

  const onFileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) handleFileSelect(file)
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
    }
  }

  const getProgressColor = () => {
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
          <DialogTitle>Bulk Upload {config.entityName}s</DialogTitle>
          <DialogDescription>
            Upload multiple {config.entityName.toLowerCase()}s at once using an Excel file
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          {/* File Upload Area */}
          <div className="border-2 border-dashed rounded-lg p-8">
            <input
              ref={fileInputRef}
              type="file"
              accept=".xlsx"
              onChange={onFileInputChange}
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
                <p className="mt-2 text-sm text-muted-foreground">Only .xlsx files are supported</p>
              </div>
            ) : (
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <FileSpreadsheet className="h-8 w-8 text-green-500" />
                  <div>
                    <p className="font-medium">{selectedFile.name}</p>
                    <p className="text-sm text-muted-foreground">{(selectedFile.size / 1024).toFixed(2)} KB</p>
                  </div>
                </div>
                {!isUploading && (
                  <Button variant="ghost" size="icon" onClick={handleRemoveFile}>
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
                <span className="text-sm text-muted-foreground">{progress.progressPercentage.toFixed(0)}%</span>
              </div>

              <Progress value={progress.progressPercentage} className={getProgressColor()} />

              <div className="grid grid-cols-4 gap-4 text-sm">
                <div className="text-center p-2 bg-muted rounded">
                  <p className="font-medium">{progress.totalRecords}</p>
                  <p className="text-muted-foreground">Total</p>
                </div>
                <div className="text-center p-2 bg-green-50 dark:bg-green-950 rounded">
                  <p className="font-medium text-green-700 dark:text-green-400">{progress.successCount}</p>
                  <p className="text-muted-foreground">Success</p>
                </div>
                <div className="text-center p-2 bg-yellow-50 dark:bg-yellow-950 rounded">
                  <p className="font-medium text-yellow-700 dark:text-yellow-400">
                    {progress.duplicateCount || 0}
                  </p>
                  <p className="text-muted-foreground">Duplicates</p>
                </div>
                <div className="text-center p-2 bg-red-50 dark:bg-red-950 rounded">
                  <p className="font-medium text-red-700 dark:text-red-400">{progress.failureCount}</p>
                  <p className="text-muted-foreground">Failed</p>
                </div>
              </div>

              {/* Errors Section */}
              {progress.errors && progress.errors.length > 0 && (
                <div className="space-y-2">
                  {(() => {
                    // Filter out duplicate errors for display (they're in the report)
                    const displayErrors = progress.errors.filter((error) => error.errorType !== 'DUPLICATE')

                    return (
                      displayErrors.length > 0 && (
                        <>
                          <Alert variant="destructive">
                            <AlertCircle className="h-4 w-4" />
                            <AlertDescription>
                              <div className="font-medium mb-2">
                                {displayErrors.length} validation error(s) found - Full error report downloaded
                                automatically
                              </div>
                              <ScrollArea className="h-32 w-full rounded border p-2 bg-background">
                                <div className="space-y-2">
                                  {displayErrors.map((error, index) => (
                                    <div key={index} className="text-sm">
                                      <span className="font-medium">Row {error.rowNumber}:</span>{' '}
                                      {error.errorType && (
                                        <span
                                          className={`inline-block px-1.5 py-0.5 rounded text-xs font-semibold mr-1 ${
                                            error.errorType === 'VALIDATION'
                                              ? 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200'
                                              : 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
                                          }`}
                                        >
                                          {error.errorType}
                                        </span>
                                      )}
                                      {error.errorMessage}
                                    </div>
                                  ))}
                                </div>
                              </ScrollArea>
                            </AlertDescription>
                          </Alert>
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={handleDownloadErrorReport}
                            className="w-full"
                          >
                            <Download className="mr-2 h-4 w-4" />
                            Download Full Error Report (Excel)
                          </Button>
                        </>
                      )
                    )
                  })()}
                </div>
              )}
            </div>
          )}

          {/* Upload Button */}
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => onOpenChange(false)} disabled={isUploading}>
              Cancel
            </Button>
            <Button onClick={handleUpload} disabled={!selectedFile || isUploading}>
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
