import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Plus, Loader2, FileUp, ChevronDown, Download, Upload, FileSpreadsheet } from 'lucide-react';
import { PermissionGuard } from '@/components/permission-guard';
import { useActivityWork } from '../context/activity-work-provider';
import { toast } from 'sonner';
import { downloadFile } from '@/lib/api-utils';
import { useExport } from '@/hooks/useExport';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

export function CreateActivityWorkButton() {
  const { openDrawer, setIsBulkUploadDialogOpen } = useActivityWork();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  
  const { isExporting, handleExport } = useExport({
    entityName: 'ActivityWork',
    exportEndpoint: '/api/activity-works/export',
  });

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile('/api/activity-works/bulk-upload/template', 'ActivityWork_Upload_Template.xlsx');
      toast.success('Template downloaded successfully');
    } catch (error) {
      toast.error('Failed to download template', {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  return (
    <div className="flex items-center gap-2">
      <PermissionGuard anyPermissions={["ACTIVITY_WORK:BULK_UPLOAD", "ACTIVITY_WORK:EXPORT"]}>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" size="sm" className="h-9 px-3">
              <FileUp className="mr-2 h-4 w-4" />
              Bulk Actions
              <ChevronDown className="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-64">
            <PermissionGuard permission="ACTIVITY_WORK:BULK_UPLOAD">
              <DropdownMenuItem onClick={handleDownloadTemplate} disabled={isDownloadingTemplate}>
                {isDownloadingTemplate ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-blue-600" />
                ) : (
                  <Download className="mr-2 h-4 w-4 text-blue-600" />
                )}
                <span>Download Template</span>
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => setIsBulkUploadDialogOpen(true)}>
                <Upload className="mr-2 h-4 w-4 text-orange-600" />
                <span>Bulk Upload</span>
              </DropdownMenuItem>
            </PermissionGuard>
            <PermissionGuard permission="ACTIVITY_WORK:EXPORT">
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
      <PermissionGuard permission="ACTIVITY_WORK:CREATE">
        <Button onClick={openDrawer} size="sm" className="h-9">
          <Plus className="mr-2 h-4 w-4" />
          Create Activity Work
        </Button>
      </PermissionGuard>
    </div>
  );
}
