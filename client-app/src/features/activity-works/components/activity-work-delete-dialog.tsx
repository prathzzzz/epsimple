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
import { useActivityWork } from '../context/activity-work-provider';
import { activityWorkApi } from '../api/activity-work-api';

export function ActivityWorkDeleteDialog() {
  const { isDeleteDialogOpen, closeDeleteDialog, selectedActivityWork } =
    useActivityWork();
  const deleteMutation = activityWorkApi.useDelete();

  const handleDelete = () => {
    if (selectedActivityWork) {
      deleteMutation.mutate(selectedActivityWork.id, {
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
            This action cannot be undone. This will permanently delete the activity work for
            <strong> {selectedActivityWork?.activitiesName}</strong> by <strong>{selectedActivityWork?.vendorName}</strong>.
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
