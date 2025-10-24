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
import { usePayeeDetails } from '../context/payee-details-provider';
import { useDeletePayeeDetails } from '../api/payee-details-api';

export function PayeeDetailsDeleteDialog() {
  const { isDeleteDialogOpen, payeeDetailsToDelete, closeDeleteDialog } =
    usePayeeDetails();
  const deleteMutation = useDeletePayeeDetails();

  const handleConfirmDelete = () => {
    if (payeeDetailsToDelete) {
      deleteMutation.mutate(payeeDetailsToDelete.id, {
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
            This will permanently delete the payee details for{' '}
            <span className="font-semibold">
              {payeeDetailsToDelete?.payeeName}
            </span>
            . This action cannot be undone.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction
            onClick={handleConfirmDelete}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            disabled={deleteMutation.isPending}
          >
            {deleteMutation.isPending ? 'Deleting...' : 'Delete'}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
