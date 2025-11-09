import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { VendorCategoriesMutateDrawer } from "./vendor-categories-mutate-drawer";
import { useVendorCategories } from "../context/vendor-categories-provider";
import { vendorCategoriesApi } from "../api/vendor-categories-api";

export function VendorCategoryDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedVendorCategory,
    setSelectedVendorCategory,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    isBulkUploadDialogOpen,
    closeBulkUploadDialog,
  } = useVendorCategories();

  const deleteMutation = useMutation({
    mutationFn: vendorCategoriesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["vendor-categories"] });
      toast.success("Vendor category deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedVendorCategory(null);
    },
  });

  const handleDelete = () => {
    if (selectedVendorCategory) {
      deleteMutation.mutate(selectedVendorCategory.id);
    }
  };

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ["vendor-categories"] });
  };

  const bulkUploadConfig = {
    entityName: "VendorCategory",
    uploadEndpoint: "/api/vendor-categories/bulk/upload",
    errorReportEndpoint: "/api/vendor-categories/bulk/export-error-report",
    onSuccess: handleBulkUploadSuccess,
  };

  return (
    <>
      <VendorCategoriesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedVendorCategory}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Vendor Category"
        desc={`Are you sure you want to delete the vendor category "${selectedVendorCategory?.categoryName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  );
}
