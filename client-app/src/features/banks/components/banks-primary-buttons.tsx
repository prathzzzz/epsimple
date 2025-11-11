import { Button } from '@/components/ui/button'
import { Download, Upload, FileDown, Plus, Loader2, ChevronDown, FileUp } from 'lucide-react'
import { downloadFile } from '@/lib/api-utils'
import { useExport } from '@/hooks/useExport'
import { useState } from 'react'
import { toast } from 'sonner'
import { useBanks } from './banks-provider'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'

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
      {/* Bulk Actions Dropdown */}
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            variant='outline'
            size='sm'
            className='h-9 px-3 text-sm font-medium'
            disabled={isDownloadingTemplate || isExporting}
          >
            <FileUp className='h-4 w-4 mr-2' />
            Bulk Actions
            <ChevronDown className='h-4 w-4 ml-2' />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align='start' className='w-64'>
          <DropdownMenuItem 
            onClick={handleDownloadTemplate} 
            className='cursor-pointer'
            disabled={isDownloadingTemplate}
          >
            {isDownloadingTemplate ? (
              <Loader2 className='h-4 w-4 mr-2 animate-spin text-blue-600' />
            ) : (
              <FileDown className='h-4 w-4 mr-2 text-blue-600' />
            )}
            <span>Download Template</span>
          </DropdownMenuItem>
          
          <DropdownMenuItem 
            onClick={openBulkUploadDialog} 
            className='cursor-pointer'
          >
            <Upload className='h-4 w-4 mr-2 text-orange-600' />
            <span>Bulk Upload</span>
          </DropdownMenuItem>
          
          <DropdownMenuSeparator />
          
          <DropdownMenuItem 
            onClick={handleExport} 
            className='cursor-pointer'
            disabled={isExporting}
          >
            {isExporting ? (
              <Loader2 className='h-4 w-4 mr-2 animate-spin text-green-600' />
            ) : (
              <Download className='h-4 w-4 mr-2 text-green-600' />
            )}
            <span>Export All Data</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      {/* Primary Action */}
      <Button 
        onClick={() => setOpen('create')}
        size='sm'
        className='h-9 px-4 text-sm font-medium'
      >
        <Plus className='h-4 w-4 mr-2' />
        Add Bank
      </Button>
    </div>
  )
}
