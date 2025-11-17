import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { toast } from 'sonner';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  Loader2,
  FileUp,
  Download,
  Upload,
  FileSpreadsheet,
  ChevronDown,
} from 'lucide-react';
import { useSiteActivityWorkExpenditure } from '../context/site-activity-work-expenditure-provider';
import { downloadFile } from '@/lib/api-utils';

export function SiteActivityWorkExpenditurePrimaryButtons() {
  const { setIsBulkUploadDialogOpen } = useSiteActivityWorkExpenditure();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  const [isExporting, setIsExporting] = useState(false);

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile(
        '/api/site-activity-work-expenditures/bulk-upload/template',
        'site-activity-work-expenditure-template.xlsx'
      );
      toast.success('Template downloaded successfully');
    } catch (error) {
      toast.error('Failed to download template', {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  const handleExportData = async () => {
    setIsExporting(true);
    try {
      const timestamp = new Date().toISOString().split('T')[0];
      await downloadFile(
        '/api/site-activity-work-expenditures/bulk-upload/export',
        `site-activity-work-expenditures-export-${timestamp}.xlsx`
      );
      toast.success('Data exported successfully');
    } catch (error) {
      toast.error('Failed to export data', {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <div className="flex items-center gap-2">
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="outline" className="h-9 px-3">
            <FileUp className="mr-2 h-4 w-4" />
            Bulk Actions
            <ChevronDown className="ml-2 h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-64">
          <DropdownMenuItem onClick={handleDownloadTemplate}>
            {isDownloadingTemplate ? (
              <Loader2 className="mr-2 h-4 w-4 animate-spin text-blue-500" />
            ) : (
              <Download className="mr-2 h-4 w-4 text-blue-500" />
            )}
            <span>Download Template</span>
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={() => setIsBulkUploadDialogOpen(true)}>
            <Upload className="mr-2 h-4 w-4 text-orange-500" />
            <span>Bulk Upload</span>
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleExportData}>
            {isExporting ? (
              <Loader2 className="mr-2 h-4 w-4 animate-spin text-green-500" />
            ) : (
              <FileSpreadsheet className="mr-2 h-4 w-4 text-green-500" />
            )}
            <span>Export All Data</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  );
}
