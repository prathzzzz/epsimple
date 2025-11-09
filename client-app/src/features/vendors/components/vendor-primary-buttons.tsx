import { Plus, Upload, Download, FileSpreadsheet } from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { Button } from '@/components/ui/button'
import { downloadFile } from '@/lib/api-utils'
import { useVendorContext } from '../vendor-provider'

export function VendorPrimaryButtons() {
  const { openCreateDrawer, openBulkUploadDialog } = useVendorContext()
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false)
  const [isExporting, setIsExporting] = useState(false)

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true)
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      await downloadFile(
        '/api/vendors/download-template',
        `Vendors_Upload_Template_${timestamp}.xlsx`
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
    setIsExporting(true)
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      await downloadFile(
        '/api/vendors/export',
        `Vendors_Export_${timestamp}.xlsx`
      )
      toast.success('Vendors exported successfully')
    } catch (error) {
      toast.error('Failed to export vendors', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsExporting(false)
    }
  }

  return (
    <div className="flex items-center gap-2">
      <Button
        variant="outline"
        onClick={handleDownloadTemplate}
        disabled={isDownloadingTemplate}
        className="border-blue-200 text-blue-700 hover:bg-blue-50 hover:text-blue-800 dark:border-blue-800 dark:text-blue-400 dark:hover:bg-blue-950 dark:hover:text-blue-300"
      >
        {isDownloadingTemplate ? (
          <Upload className="mr-2 h-4 w-4 animate-spin" />
        ) : (
          <Download className="mr-2 h-4 w-4" />
        )}
        Download Template
      </Button>
      <Button
        variant="outline"
        onClick={handleExport}
        disabled={isExporting}
        className="border-green-200 text-green-700 hover:bg-green-50 hover:text-green-800 dark:border-green-800 dark:text-green-400 dark:hover:bg-green-950 dark:hover:text-green-300"
      >
        {isExporting ? (
          <Upload className="mr-2 h-4 w-4 animate-spin" />
        ) : (
          <FileSpreadsheet className="mr-2 h-4 w-4" />
        )}
        Export All
      </Button>
      <Button
        variant="outline"
        onClick={openBulkUploadDialog}
        className="border-purple-200 text-purple-700 hover:bg-purple-50 hover:text-purple-800 dark:border-purple-800 dark:text-purple-400 dark:hover:bg-purple-950 dark:hover:text-purple-300"
      >
        <Upload className="mr-2 h-4 w-4" />
        Bulk Upload
      </Button>
      <Button onClick={openCreateDrawer}>
        <Plus className="mr-2 h-4 w-4" />
        Create Vendor
      </Button>
    </div>
  )
}
