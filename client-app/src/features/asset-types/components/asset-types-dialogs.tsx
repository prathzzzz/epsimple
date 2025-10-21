import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { ConfirmDialog } from '@/components/confirm-dialog';
import { AssetTypesMutateDrawer } from './asset-types-mutate-drawer';
import { useAssetTypes } from '../context/asset-types-provider';
import { assetTypesApi } from '../api/asset-types-api';

export function AssetTypesDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedAssetType,
    setSelectedAssetType,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
  } = useAssetTypes();

  const deleteMutation = useMutation({
    mutationFn: assetTypesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['asset-types'] });
      toast.success('Asset type deleted successfully');
      setIsDeleteDialogOpen(false);
      setSelectedAssetType(null);
    },
  });

  const handleDelete = () => {
    if (selectedAssetType) {
      deleteMutation.mutate(selectedAssetType.id);
    }
  };

  return (
    <>
      <AssetTypesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedAssetType}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Asset Type"
        desc={`Are you sure you want to delete the asset type "${selectedAssetType?.typeName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
    </>
  );
}
