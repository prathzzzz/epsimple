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
