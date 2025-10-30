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
import { useAssetTagCodeGeneratorContext } from "../context/asset-tag-generator-provider";
import { assetTagCodeGeneratorApi } from "../api/asset-tag-generator-api";

export function AssetTagCodeGeneratorDeleteDialog() {
  const { isDeleteDialogOpen, setIsDeleteDialogOpen, generatorToDelete, setGeneratorToDelete } =
    useAssetTagCodeGeneratorContext();

  const deleteMutation = assetTagCodeGeneratorApi.useDelete();

  const handleDelete = async () => {
    if (!generatorToDelete) return;

    try {
      await deleteMutation.mutateAsync(generatorToDelete.id);
      setIsDeleteDialogOpen(false);
      setGeneratorToDelete(null);
    } catch (error) {
      // Error handled by mutation
    }
  };

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This will permanently delete the asset tag generator for{" "}
            <strong>
              {generatorToDelete?.assetCategoryName} - {generatorToDelete?.vendorName} - {generatorToDelete?.bankName}
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
