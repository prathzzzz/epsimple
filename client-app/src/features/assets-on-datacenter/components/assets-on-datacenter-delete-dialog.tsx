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
import { useAssetsOnDatacenter } from '../context/assets-on-datacenter-provider';
import { assetsOnDatacenterApi } from '../api/assets-on-datacenter-api';

export function AssetsOnDatacenterDeleteDialog() {
  const { isDeleteDialogOpen, closeDeleteDialog, selectedPlacement } =
    useAssetsOnDatacenter();
  const deleteMutation = assetsOnDatacenterApi.useDelete();

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
            <strong>{selectedPlacement?.assetTagId}</strong> from Datacenter{' '}
            <strong>{selectedPlacement?.datacenterName}</strong>.
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
