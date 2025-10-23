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
import { useManagedProjectContext } from "../context/managed-project-provider";
import { managedProjectApi } from "../api/managed-project-api";

export function ManagedProjectDeleteDialog() {
  const { isDeleteDialogOpen, setIsDeleteDialogOpen, deletingManagedProjectId, setDeletingManagedProjectId } =
    useManagedProjectContext();

  const deleteMutation = managedProjectApi.useDelete();

  const handleDelete = async () => {
    if (deletingManagedProjectId) {
      await deleteMutation.mutateAsync(deletingManagedProjectId);
      setIsDeleteDialogOpen(false);
      setDeletingManagedProjectId(null);
    }
  };

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete the managed project.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={() => setDeletingManagedProjectId(null)}>
            Cancel
          </AlertDialogCancel>
          <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">
            Delete
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
