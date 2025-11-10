import { Download, FileUp, Plus, Upload } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useMovementTypes } from '../context/movement-types-provider';
import { toast } from 'sonner';

export function MovementTypesPrimaryButtons() {
  const { setIsBulkUploadDialogOpen, setSelectedMovementType, setIsDrawerOpen } = useMovementTypes();

  const handleCreate = () => {
    setSelectedMovementType(null);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    try {
      const response = await fetch('/api/movement-types/bulk-upload/template', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to download template');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'MovementType_BulkUpload_Template.xlsx';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);

      toast.success('Template downloaded successfully');
    } catch (error) {
      console.error('Error downloading template:', error);
      toast.error('Failed to download template');
    }
  };

  const handleExportData = async () => {
    try {
      const response = await fetch('/api/movement-types/bulk-upload/export', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to export data');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'MovementTypes_Export.xlsx';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);

      toast.success('Data exported successfully');
    } catch (error) {
      console.error('Error exporting data:', error);
      toast.error('Failed to export data');
    }
  };

  return (
    <div className="flex flex-wrap gap-2">
      <Button
        variant="outline"
        size="sm"
        onClick={handleDownloadTemplate}
        className="border-blue-500 text-blue-600 hover:bg-blue-50 dark:border-blue-400 dark:text-blue-400 dark:hover:bg-blue-950"
      >
        <Download className="h-4 w-4 mr-2" />
        Download Template
      </Button>

      <Button
        variant="outline"
        size="sm"
        onClick={() => setIsBulkUploadDialogOpen(true)}
        className="border-green-500 text-green-600 hover:bg-green-50 dark:border-green-400 dark:text-green-400 dark:hover:bg-green-950"
      >
        <FileUp className="h-4 w-4 mr-2" />
        Bulk Upload
      </Button>

      <Button
        variant="outline"
        size="sm"
        onClick={handleExportData}
        className="border-purple-500 text-purple-600 hover:bg-purple-50 dark:border-purple-400 dark:text-purple-400 dark:hover:bg-purple-950"
      >
        <Upload className="h-4 w-4 mr-2" />
        Export Data
      </Button>

      <Button onClick={handleCreate}>
        <Plus className="h-4 w-4 mr-2" />
        Add Movement Type
      </Button>
    </div>
  );
}
