import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { useCostItemContext } from "./cost-item-provider";
import { useQueryClient } from "@tanstack/react-query";

export function CostItemsDialogs() {
  const queryClient = useQueryClient();
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } = useCostItemContext();

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ["cost-items"] });
  };

  const bulkUploadConfig = {
    entityName: "CostItem",
    uploadEndpoint: "/api/cost-items/bulk-upload",
    errorReportEndpoint: "/api/cost-items/export-errors",
    onSuccess: handleBulkUploadSuccess,
  };

  return (
    <>
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  );
}
