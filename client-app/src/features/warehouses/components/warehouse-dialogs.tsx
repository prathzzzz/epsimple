import { useQueryClient } from '@tanstack/react-query';
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog';
import { useWarehouse } from '../hooks/use-warehouse';
import { WarehouseDrawer } from './warehouse-drawer';
import { WarehouseDeleteDialog } from './warehouse-delete-dialog';

export function WarehouseDialogs() {
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } = useWarehouse();
  const queryClient = useQueryClient();

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['warehouses'] });
  };

  const bulkUploadConfig = {
    entityName: 'Warehouse',
    uploadEndpoint: '/api/warehouses/bulk/upload',
    errorReportEndpoint: '/api/warehouses/bulk/export-error-report',
    onSuccess: handleBulkUploadSuccess,
  };

  return (
    <>
      <WarehouseDrawer />
      <WarehouseDeleteDialog />

      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  );
}
