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
import { useSiteContext } from "../context/site-provider";
import { siteApi } from "../api/site-api";

export function SiteDeleteDialog() {
  const { isDeleteDialogOpen, setIsDeleteDialogOpen, deletingSiteId, setDeletingSiteId } =
    useSiteContext();

  const deleteMutation = siteApi.useDelete();

  const handleDelete = () => {
    if (deletingSiteId) {
      deleteMutation.mutate(deletingSiteId, {
        onSuccess: () => {
          setIsDeleteDialogOpen(false);
          setDeletingSiteId(null);
        },
      });
    }
  };

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete the site
            and all associated data.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={() => setDeletingSiteId(null)}>
            Cancel
          </AlertDialogCancel>
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
