import { Button } from '@/components/ui/button'
import { Plus, Upload, FileSpreadsheet, Loader2, Download } from 'lucide-react'
import { useStates } from './states-provider'
import { useExport } from '@/hooks/useExport'
import { useState } from 'react'
import { toast } from 'sonner'
import { downloadFile } from '@/lib/api-utils'

export function StatesPrimaryButtons() {
  const { openDrawer, setIsEditMode, openBulkUploadDialog } = useStates()
  const { isExporting, handleExport } = useExport({
    entityName: 'State',
    exportEndpoint: '/api/states/export',
  })
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false)

  const handleCreateClick = () => {
    setIsEditMode(false)
    openDrawer()
  }

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true)
    try {
      await downloadFile('/api/states/download-template', 'State_Upload_Template.xlsx')
      toast.success('Template downloaded successfully')
    } catch (error) {
      toast.error('Failed to download template', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsDownloadingTemplate(false)
    }
  }

  return (
    <div className="flex gap-2">
      <Button onClick={handleDownloadTemplate} size='sm' variant="outline" className="border-blue-500 text-blue-600 hover:bg-blue-50 dark:border-blue-600 dark:text-blue-400 dark:hover:bg-blue-950" disabled={isDownloadingTemplate}>
        {isDownloadingTemplate ? (
          <Loader2 className='mr-2 h-4 w-4 animate-spin' />
        ) : (
          <Download className='mr-2 h-4 w-4' />
        )}
        Download Template
      </Button>
      <Button onClick={handleExport} size='sm' variant="outline" className="border-green-500 text-green-600 hover:bg-green-50 dark:border-green-600 dark:text-green-400 dark:hover:bg-green-950" disabled={isExporting}>
        {isExporting ? (
          <Loader2 className='mr-2 h-4 w-4 animate-spin' />
        ) : (
          <FileSpreadsheet className='mr-2 h-4 w-4' />
        )}
        Export
      </Button>
      <Button onClick={openBulkUploadDialog} size='sm' variant="outline" className="border-orange-500 text-orange-600 hover:bg-orange-50 dark:border-orange-600 dark:text-orange-400 dark:hover:bg-orange-950">
        <Upload className='mr-2 h-4 w-4' />
        Bulk Upload
      </Button>
      <Button onClick={handleCreateClick} size='sm'>
        <Plus className='mr-2 h-4 w-4' />
        Add State
      </Button>
    </div>
  )
}
