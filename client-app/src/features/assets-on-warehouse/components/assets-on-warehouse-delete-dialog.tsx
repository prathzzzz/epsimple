import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import { useAssetsOnWarehouse } from '../context/assets-on-warehouse-provider';
import { assetsOnWarehouseApi } from '../api/assets-on-warehouse-api';

export function AssetsOnWarehouseDeleteDialog() {
  const { isDeleteDialogOpen, closeDeleteDialog, selectedPlacement } =
    useAssetsOnWarehouse();
  const deleteMutation = assetsOnWarehouseApi.useDelete();

  const handleDelete = () => {
    if (selectedPlacement) {
      deleteMutation.mutate(selectedPlacement.id, {
        onSuccess: () => {
          closeDeleteDialog();
        },
      });
    }
  };

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={closeDeleteDialog}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will remove asset{' '}
            <strong>{selectedPlacement?.assetTagId}</strong> from warehouse{' '}
            <strong>{selectedPlacement?.warehouseName}</strong>.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction onClick={handleDelete} disabled={deleteMutation.isPending}>
            {deleteMutation.isPending ? 'Removing...' : 'Remove'}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
