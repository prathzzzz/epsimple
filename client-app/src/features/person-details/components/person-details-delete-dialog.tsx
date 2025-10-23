import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { usePersonDetailsContext } from "../context/person-details-provider";
import { personDetailsApi } from "../api/person-details-api";

export function PersonDetailsDeleteDialog() {
  const {
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    deletingPersonDetailsId,
    setDeletingPersonDetailsId,
  } = usePersonDetailsContext();

  const deleteMutation = personDetailsApi.useDelete();

  const handleDelete = () => {
    if (deletingPersonDetailsId) {
      deleteMutation.mutate(deletingPersonDetailsId, {
        onSuccess: () => {
          setIsDeleteDialogOpen(false);
          setDeletingPersonDetailsId(null);
        },
      });
    }
  };

  const handleCancel = () => {
    setIsDeleteDialogOpen(false);
    setDeletingPersonDetailsId(null);
  };

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete the person
            details from the database.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={handleCancel}>Cancel</AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            Delete
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
