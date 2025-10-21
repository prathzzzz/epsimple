import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { CostCategoriesMutateDrawer } from "./cost-categories-mutate-drawer";
import { useCostCategories } from "../context/cost-categories-provider";
import { costCategoriesApi } from "../api/cost-categories-api";

export function CostCategoriesDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedCostCategory,
    setSelectedCostCategory,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
  } = useCostCategories();

  const deleteMutation = useMutation({
    mutationFn: costCategoriesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["cost-categories"] });
      toast.success("Cost category deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedCostCategory(null);
    },
  });

  const handleDelete = () => {
    if (selectedCostCategory) {
      deleteMutation.mutate(selectedCostCategory.id);
    }
  };

  return (
    <>
      <CostCategoriesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedCostCategory}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Cost Category"
        desc={`Are you sure you want to delete the cost category "${selectedCostCategory?.categoryName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
    </>
  );
}
