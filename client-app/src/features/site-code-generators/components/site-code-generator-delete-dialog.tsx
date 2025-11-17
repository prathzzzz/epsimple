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
import { useSiteCodeGeneratorContext } from "../context/site-code-generator-provider";
import { siteCodeGeneratorApi } from "../api/site-code-generator-api";

export function SiteCodeGeneratorDeleteDialog() {
  const { isDeleteDialogOpen, setIsDeleteDialogOpen, generatorToDelete, setGeneratorToDelete } =
    useSiteCodeGeneratorContext();

  const deleteMutation = siteCodeGeneratorApi.useDelete();

  const handleDelete = async () => {
    if (!generatorToDelete) return;

    try {
      await deleteMutation.mutateAsync(generatorToDelete.id);
      setIsDeleteDialogOpen(false);
      setGeneratorToDelete(null);
    } catch (_error) {
      // Error handled by mutation
    }
  };

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This will permanently delete the site code generator for{" "}
            <strong>
              {generatorToDelete?.projectName} - {generatorToDelete?.stateName}
            </strong>
            . This action cannot be undone.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={() => setGeneratorToDelete(null)}>Cancel</AlertDialogCancel>
          <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">
            Delete
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
