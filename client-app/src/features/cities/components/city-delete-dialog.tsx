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
import { useCityContext } from "../context/city-provider";
import { cityApi } from "../api/city-api";

export function CityDeleteDialog() {
  const {
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    editingCity,
    setEditingCity,
  } = useCityContext();

  const deleteMutation = cityApi.useDelete();

  const handleDelete = () => {
    if (editingCity) {
      deleteMutation.mutate(editingCity.id, {
        onSuccess: () => {
          setIsDeleteDialogOpen(false);
          setEditingCity(null);
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
            This action cannot be undone. This will permanently delete the city
            "{editingCity?.cityName}".
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={() => setEditingCity(null)}>
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
