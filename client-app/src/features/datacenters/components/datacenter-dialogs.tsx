import { useQueryClient } from '@tanstack/react-query';
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog';
import { useDatacenter } from '../hooks/use-datacenter';
import { DatacenterDrawer } from './datacenter-drawer';
import { DatacenterDeleteDialog } from './datacenter-delete-dialog';

export function DatacenterDialogs() {
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } = useDatacenter();
  const queryClient = useQueryClient();

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['datacenters'] });
  };

  const bulkUploadConfig = {
    entityName: 'Datacenter',
    uploadEndpoint: '/api/datacenters/bulk/upload',
    errorReportEndpoint: '/api/datacenters/bulk/export-error-report',
    onSuccess: handleBulkUploadSuccess,
  };

  return (
    <>
      <DatacenterDrawer />
      <DatacenterDeleteDialog />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  );
}
