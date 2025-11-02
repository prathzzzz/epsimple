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
import { useAssetsOnSite } from '../context/assets-on-site-provider';
import { assetsOnSiteApi } from '../api/assets-on-site-api';

export function AssetsOnSiteDeleteDialog() {
  const { isDeleteDialogOpen, closeDeleteDialog, selectedPlacement } =
    useAssetsOnSite();
  const deleteMutation = assetsOnSiteApi.useDelete();

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
            <strong>{selectedPlacement?.assetTagId}</strong> from site{' '}
            <strong>{selectedPlacement?.siteCode}</strong>.
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
