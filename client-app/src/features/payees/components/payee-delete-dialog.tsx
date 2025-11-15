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
import { usePayee } from '../hooks/use-payee';
import { payeeApi } from '../api/payee-api';

export function PayeeDeleteDialog() {
  const { isDeleteDialogOpen, closeDeleteDialog, selectedPayee } = usePayee();
  const deleteMutation = payeeApi.useDelete();

  const handleDelete = () => {
    if (selectedPayee) {
      deleteMutation.mutate(selectedPayee.id, {
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
            This will permanently delete the payee <strong>{selectedPayee?.payeeName}</strong>.
            This action cannot be undone.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction onClick={handleDelete} disabled={deleteMutation.isPending}>
            {deleteMutation.isPending ? 'Deleting...' : 'Delete'}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
