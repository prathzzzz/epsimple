import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
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
import { useSiteTypeContext } from "../context/site-type-provider";
import { siteTypeApi } from "../api/site-type-api";

export function SiteTypeDialogs() {
  const queryClient = useQueryClient();
  const {
    editingSiteType,
    setEditingSiteType,
    showDeleteDialog,
    setShowDeleteDialog,
  } = useSiteTypeContext();

  const deleteMutation = useMutation({
    mutationFn: (id: number) => siteTypeApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["site-types"] });
      toast.success("Site type deleted successfully");
      setShowDeleteDialog(false);
      setEditingSiteType(null);
    },
    onError: (error: any) => {
      const message = error?.response?.data?.message || error?.message || "Failed to delete site type";
      toast.error(message);
    },
  });

  const handleDelete = () => {
    if (editingSiteType) {
      deleteMutation.mutate(editingSiteType.id);
    }
  };

  return (
    <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete the site
            type
            {editingSiteType && (
              <span className="font-semibold">
                {" "}
                &quot;{editingSiteType.typeName}&quot;
              </span>
            )}
            .
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel disabled={deleteMutation.isPending}>
            Cancel
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete}
            disabled={deleteMutation.isPending}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            {deleteMutation.isPending ? "Deleting..." : "Delete"}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
