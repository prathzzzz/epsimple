import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { ConfirmDialog } from '@/components/confirm-dialog';
import { VendorTypesMutateDrawer } from './vendor-types-mutate-drawer';
import { useVendorTypes } from '../context/vendor-types-provider';
import { vendorTypesApi } from '../api/vendor-types-api';

export function VendorTypesDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedVendorType,
    setSelectedVendorType,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
  } = useVendorTypes();

  const deleteMutation = useMutation({
    mutationFn: vendorTypesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vendor-types'] });
      toast.success('Vendor type deleted successfully');
      setIsDeleteDialogOpen(false);
      setSelectedVendorType(null);
    },
  });

  const handleDelete = () => {
    if (selectedVendorType) {
      deleteMutation.mutate(selectedVendorType.id);
    }
  };

  return (
    <>
      <VendorTypesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedVendorType}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Vendor Type"
        desc={`Are you sure you want to delete the vendor type "${selectedVendorType?.typeName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
    </>
  );
}
