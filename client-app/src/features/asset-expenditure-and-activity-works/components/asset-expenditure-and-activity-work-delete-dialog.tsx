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
import { useAssetExpenditureAndActivityWork } from '../context/asset-expenditure-and-activity-work-provider';
import { assetExpenditureAndActivityWorkApi } from '../api/asset-expenditure-and-activity-work-api';

export function AssetExpenditureAndActivityWorkDeleteDialog() {
  const { isDeleteDialogOpen, closeDeleteDialog, selectedExpenditure } =
    useAssetExpenditureAndActivityWork();
  const deleteMutation = assetExpenditureAndActivityWorkApi.useDelete();

  const handleDelete = async () => {
    if (!selectedExpenditure) return;

    deleteMutation.mutate(selectedExpenditure.id, {
      onSuccess: () => {
        closeDeleteDialog();
      },
    });
  };

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={closeDeleteDialog}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete the expenditure
            link for asset <strong>{selectedExpenditure?.assetTagId}</strong>.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            {deleteMutation.isPending ? 'Deleting...' : 'Delete'}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
