import { Button } from '@/components/ui/button'
import { Download, Upload, FileDown, Plus, Loader2 } from 'lucide-react'
import { downloadFile } from '@/lib/api-utils'
import { useExport } from '@/hooks/useExport'
import { useState } from 'react'
import { toast } from 'sonner'
import { useBanks } from './banks-provider'

export function BanksPrimaryButtons() {
  const { setOpen, openBulkUploadDialog } = useBanks()
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false)

  const { isExporting, handleExport } = useExport({
    entityName: 'Bank',
    exportEndpoint: '/api/banks/bulk/export-data',
  })

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true)
    try {
      await downloadFile(
        '/api/banks/bulk/export-template',
        'Bank_Upload_Template.xlsx'
      )
      toast.success('Template downloaded successfully')
    } catch (error) {
      console.error('Failed to download template:', error)
      toast.error('Failed to download template', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsDownloadingTemplate(false)
    }
  }

  return (
    <div className='flex items-center gap-2'>
      <Button
        onClick={handleDownloadTemplate}
        variant='outline'
        size='sm'
        className='h-8 border-blue-500 text-blue-600 hover:bg-blue-50 dark:border-blue-600 dark:text-blue-400 dark:hover:bg-blue-950'
        disabled={isDownloadingTemplate}
      >
        {isDownloadingTemplate ? (
          <Loader2 className='mr-2 size-4 animate-spin' />
        ) : (
          <FileDown className='mr-2 size-4' />
        )}
        Download Template
      </Button>

      <Button
        onClick={handleExport}
        disabled={isExporting}
        variant='outline'
        size='sm'
        className='h-8 border-green-500 text-green-600 hover:bg-green-50 dark:border-green-600 dark:text-green-400 dark:hover:bg-green-950'
      >
        {isExporting ? (
          <Loader2 className='mr-2 size-4 animate-spin' />
        ) : (
          <Download className='mr-2 size-4' />
        )}
        Export
      </Button>

      <Button
        onClick={openBulkUploadDialog}
        variant='outline'
        size='sm'
        className='h-8 border-orange-500 text-orange-600 hover:bg-orange-50 dark:border-orange-600 dark:text-orange-400 dark:hover:bg-orange-950'
      >
        <Upload className='mr-2 size-4' />
        Bulk Upload
      </Button>

      <Button onClick={() => setOpen('create')} size='sm' className='h-8'>
        <Plus className='mr-2 size-4' />
        Add Bank
      </Button>
    </div>
  )
}
