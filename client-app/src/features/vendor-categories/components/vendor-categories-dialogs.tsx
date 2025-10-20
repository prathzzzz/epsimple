import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { ConfirmDialog } from '@/components/confirm-dialog';
import { VendorCategoriesMutateDrawer } from './vendor-categories-mutate-drawer';
import { useVendorCategories } from '../context/vendor-categories-provider';
import { vendorCategoriesApi } from '../api/vendor-categories-api';
import { handleServerError } from '@/lib/handle-server-error';

export function VendorCategoriesDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedVendorCategory,
    setSelectedVendorCategory,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
  } = useVendorCategories();

  const deleteMutation = useMutation({
    mutationFn: vendorCategoriesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vendor-categories'] });
      toast.success('Vendor category deleted successfully');
      setIsDeleteDialogOpen(false);
      setSelectedVendorCategory(null);
    },
    onError: (error) => {
      const errorMessage = handleServerError(error);
      toast.error(errorMessage?.message || 'Failed to delete vendor category');
    },
  });

  const handleDelete = () => {
    if (selectedVendorCategory) {
      deleteMutation.mutate(selectedVendorCategory.id);
    }
  };

  return (
    <>
      <VendorCategoriesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedVendorCategory}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Vendor Category"
        desc={`Are you sure you want to delete the vendor category "${selectedVendorCategory?.categoryName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
    </>
  );
}
