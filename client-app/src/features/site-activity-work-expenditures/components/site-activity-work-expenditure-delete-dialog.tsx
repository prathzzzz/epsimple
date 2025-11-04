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
import { useSiteActivityWorkExpenditure } from '../context/site-activity-work-expenditure-provider';
import { siteActivityWorkExpenditureApi } from '../api/site-activity-work-expenditure-api';

export function SiteActivityWorkExpenditureDeleteDialog() {
  const { isDeleteDialogOpen, closeDeleteDialog, selectedExpenditure } =
    useSiteActivityWorkExpenditure();
  const deleteMutation = siteActivityWorkExpenditureApi.useDelete();

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
            This action cannot be undone. This will permanently delete the link
            between <strong>{selectedExpenditure?.siteCode}</strong>,{' '}
            <strong>{selectedExpenditure?.activityName}</strong>, and invoice{' '}
            <strong>{selectedExpenditure?.invoiceNumber}</strong>.
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
