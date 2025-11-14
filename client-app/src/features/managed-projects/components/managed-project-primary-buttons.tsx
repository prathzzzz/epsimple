import { Download, Upload, Plus, Loader2, FileUp, ChevronDown, FileSpreadsheet } from 'lucide-react'
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { downloadFile } from '@/lib/api-utils'
import { useExport } from '@/hooks/useExport'
import { useState } from 'react'
import { toast } from 'sonner'
import { useManagedProjectContext } from '../context/managed-project-provider'

export function ManagedProjectPrimaryButtons() {
  const { setIsDrawerOpen, openBulkUploadDialog } = useManagedProjectContext()
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false)

  const { isExporting, handleExport } = useExport({
    entityName: 'ManagedProject',
    exportEndpoint: '/api/managed-projects/export',
  })

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true)
    try {
      await downloadFile(
        '/api/managed-projects/bulk/export-template',
        'ManagedProject_Upload_Template.xlsx'
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

  return (
    <div className='flex items-center gap-2'>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant='outline' size='sm' className='h-9 px-3'>
            <FileUp className='mr-2 h-4 w-4' />
            Bulk Actions
            <ChevronDown className='ml-2 h-4 w-4' />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align='start' className='w-64'>
          <DropdownMenuItem onClick={handleDownloadTemplate} disabled={isDownloadingTemplate}>
            {isDownloadingTemplate ? (
              <Loader2 className='mr-2 h-4 w-4 animate-spin text-blue-600' />
            ) : (
              <Download className='mr-2 h-4 w-4 text-blue-600' />
            )}
            <span>Download Template</span>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={openBulkUploadDialog}>
            <Upload className='mr-2 h-4 w-4 text-orange-600' />
            <span>Bulk Upload</span>
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleExport} disabled={isExporting}>
            {isExporting ? (
              <Loader2 className='mr-2 h-4 w-4 animate-spin text-green-600' />
            ) : (
              <FileSpreadsheet className='mr-2 h-4 w-4 text-green-600' />
            )}
            <span>Export All Data</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      <Button onClick={() => setIsDrawerOpen(true)} size='sm' className='h-9'>
        <Plus className='mr-2 h-4 w-4' />
        Create Managed Project
      </Button>
    </div>
  )
}
