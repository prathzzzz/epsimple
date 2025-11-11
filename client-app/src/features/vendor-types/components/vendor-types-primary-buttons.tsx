import { Plus, Upload, Download, FileSpreadsheet, ChevronDown, Loader2, FileUp } from 'lucide-react';
import { useState } from 'react';
import { toast } from 'sonner';
import { Button } from '@/components/ui/button';
import { downloadFile } from '@/lib/api-utils';
import { useVendorTypes } from '../context/vendor-types-provider';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

export function VendorTypesPrimaryButtons() {
  const { setSelectedVendorType, setIsDrawerOpen, openBulkUploadDialog } = useVendorTypes();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  const [isExporting, setIsExporting] = useState(false);

  const handleCreate = () => {
    setSelectedVendorType(null);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5);
      await downloadFile(
        '/api/vendor-types/download-template',
        `VendorType_Upload_Template_${timestamp}.xlsx`
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

  const handleExport = async () => {
    setIsExporting(true);
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-').slice(0, -5);
      await downloadFile(
        '/api/vendor-types/export',
        `VendorTypes_Export_${timestamp}.xlsx`
      );
      toast.success('Vendor types exported successfully');
    } catch (error) {
      toast.error('Failed to export vendor types', {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <div className="flex items-center gap-2">
      {/* Bulk Actions Dropdown */}
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            variant="outline"
            size="sm"
            className="h-9 px-3 text-sm font-medium"
            disabled={isDownloadingTemplate || isExporting}
          >
            <FileUp className="h-4 w-4 mr-2" />
            Bulk Actions
            <ChevronDown className="h-4 w-4 ml-2" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="start" className="w-64">
          <DropdownMenuItem 
            onClick={handleDownloadTemplate} 
            className="cursor-pointer"
            disabled={isDownloadingTemplate}
          >
            {isDownloadingTemplate ? (
              <Loader2 className="h-4 w-4 mr-2 animate-spin text-blue-600" />
            ) : (
              <Download className="h-4 w-4 mr-2 text-blue-600" />
            )}
            <span>Download Template</span>
          </DropdownMenuItem>
          
          <DropdownMenuItem 
            onClick={openBulkUploadDialog} 
            className="cursor-pointer"
          >
            <Upload className="h-4 w-4 mr-2 text-orange-600" />
            <span>Bulk Upload</span>
          </DropdownMenuItem>
          
          <DropdownMenuSeparator />
          
          <DropdownMenuItem 
            onClick={handleExport} 
            className="cursor-pointer"
            disabled={isExporting}
          >
            {isExporting ? (
              <Loader2 className="h-4 w-4 mr-2 animate-spin text-green-600" />
            ) : (
              <FileSpreadsheet className="h-4 w-4 mr-2 text-green-600" />
            )}
            <span>Export All Data</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      {/* Primary Action */}
      <Button 
        onClick={handleCreate}
        size="sm"
        className="h-9 px-4 text-sm font-medium"
      >
        <Plus className="h-4 w-4 mr-2" />
        Add Vendor Type
      </Button>
    </div>
  );
}
