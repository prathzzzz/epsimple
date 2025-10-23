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
import { useSiteCategoryContext } from "../context/site-category-provider";
import { siteCategoryApi } from "../api/site-category-api";

export function SiteCategoryDeleteDialog() {
  const {
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    editingCategory,
    setEditingCategory,
  } = useSiteCategoryContext();

  const deleteMutation = siteCategoryApi.useDelete();

  const handleDelete = () => {
    if (editingCategory) {
      deleteMutation.mutate(editingCategory.id, {
        onSuccess: () => {
          setIsDeleteDialogOpen(false);
          setEditingCategory(null);
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
            category "{editingCategory?.categoryName}".
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={() => setEditingCategory(null)}>
            Cancel
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete}
            disabled={deleteMutation.isPending}
          >
            {deleteMutation.isPending ? "Deleting..." : "Delete"}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
