import { Plus, Upload, Download, FileSpreadsheet, Loader2, FileUp, ChevronDown } from 'lucide-react'
import { useState } from 'react'
import { toast } from 'sonner'
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { PermissionGuard } from '@/components/permission-guard'
import { downloadFile } from '@/lib/api-utils'
import { usePersonDetailsContext } from '../context/person-details-provider'

export function PersonDetailsPrimaryButtons() {
  const { setIsDrawerOpen, setEditingPersonDetails, openBulkUploadDialog } = usePersonDetailsContext()
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false)
  const [isExporting, setIsExporting] = useState(false)

  const handleAddNew = () => {
    setEditingPersonDetails(null)
    setIsDrawerOpen(true)
  }

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true)
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5)
      await downloadFile(
        '/api/person-details/download-template',
        `PersonDetails_Upload_Template_${timestamp}.xlsx`
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
        '/api/person-details/export',
        `PersonDetails_Export_${timestamp}.xlsx`
      )
      toast.success('Person details exported successfully')
    } catch (error) {
      toast.error('Failed to export person details', {
        description: error instanceof Error ? error.message : 'An error occurred',
      })
    } finally {
      setIsExporting(false)
    }
  }

  return (
    <div className="flex items-center gap-2">
      <PermissionGuard anyPermissions={['PERSON_DETAILS:BULK_UPLOAD', 'PERSON_DETAILS:EXPORT']}>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" size="sm" className="h-9 px-3">
              <FileUp className="mr-2 h-4 w-4" />
              Bulk Actions
              <ChevronDown className="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="start" className="w-64">
            <PermissionGuard permission="PERSON_DETAILS:EXPORT">
              <DropdownMenuItem onClick={handleDownloadTemplate} disabled={isDownloadingTemplate}>
                {isDownloadingTemplate ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-blue-600" />
                ) : (
                  <Download className="mr-2 h-4 w-4 text-blue-600" />
                )}
                <span>Download Template</span>
              </DropdownMenuItem>
            </PermissionGuard>
            <PermissionGuard permission="PERSON_DETAILS:BULK_UPLOAD">
              <DropdownMenuItem onClick={openBulkUploadDialog}>
                <Upload className="mr-2 h-4 w-4 text-orange-600" />
                <span>Bulk Upload</span>
              </DropdownMenuItem>
            </PermissionGuard>
            <PermissionGuard permission="PERSON_DETAILS:EXPORT">
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleExport} disabled={isExporting}>
                {isExporting ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-green-600" />
                ) : (
                  <FileSpreadsheet className="mr-2 h-4 w-4 text-green-600" />
                )}
                <span>Export All Data</span>
              </DropdownMenuItem>
            </PermissionGuard>
          </DropdownMenuContent>
        </DropdownMenu>
      </PermissionGuard>

      <PermissionGuard permission="PERSON_DETAILS:CREATE">
        <Button onClick={handleAddNew} size="sm" className="h-9">
          <Plus className="mr-2 h-4 w-4" />
          Create Person
        </Button>
      </PermissionGuard>
    </div>
  )
}
