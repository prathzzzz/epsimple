import { Download, Upload, FileSpreadsheet, Plus, Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { downloadFile } from '@/lib/api-utils';
import { useExport } from '@/hooks/useExport';
import { useWarehouse } from '../context/warehouse-provider';
import { useState } from 'react';
import { toast } from 'sonner';

export function WarehousePrimaryButtons() {
  const { openDrawer, openBulkUploadDialog } = useWarehouse();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);

  const { isExporting, handleExport } = useExport({
    entityName: 'Warehouse',
    exportEndpoint: '/api/warehouses/bulk/export-data',
  });

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile(
        '/api/warehouses/bulk/export-template',
        'Warehouse_Upload_Template.xlsx'
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

  return (
    <div className="flex gap-2">
      <Button
        onClick={handleDownloadTemplate}
        variant="outline"
        className="border-blue-500 text-blue-600 hover:bg-blue-50 dark:border-blue-600 dark:text-blue-400 dark:hover:bg-blue-950"
        size="sm"
        disabled={isDownloadingTemplate}
      >
        {isDownloadingTemplate ? (
          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
        ) : (
          <Download className="mr-2 h-4 w-4" />
        )}
        Download Template
      </Button>

      <Button
        onClick={handleExport}
        disabled={isExporting}
        variant="outline"
        className="border-green-500 text-green-600 hover:bg-green-50 dark:border-green-600 dark:text-green-400 dark:hover:bg-green-950"
        size="sm"
      >
        {isExporting ? (
          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
        ) : (
          <FileSpreadsheet className="mr-2 h-4 w-4" />
        )}
        Export
      </Button>

      <Button
        onClick={openBulkUploadDialog}
        variant="outline"
        className="border-orange-500 text-orange-600 hover:bg-orange-50 dark:border-orange-600 dark:text-orange-400 dark:hover:bg-orange-950"
        size="sm"
      >
        <Upload className="mr-2 h-4 w-4" />
        Bulk Upload
      </Button>

      <Button onClick={openDrawer} size="sm">
        <Plus className="mr-2 h-4 w-4" />
        Add Warehouse
      </Button>
    </div>
  );
}
