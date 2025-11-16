import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { useAssetExpenditureAndActivityWork } from "../context/asset-expenditure-and-activity-work-provider";
import { AssetExpenditureAndActivityWorkDeleteDialog } from "./asset-expenditure-and-activity-work-delete-dialog";
import { useQueryClient } from "@tanstack/react-query";

export function AssetExpenditureAndActivityWorkDialogs() {
  const { isBulkUploadDialogOpen, closeBulkUploadDialog } =
    useAssetExpenditureAndActivityWork();
  const queryClient = useQueryClient();

  const handleBulkUploadSuccess = () => {
    // Invalidate queries to refresh the data
    queryClient.invalidateQueries({
      queryKey: ["assetExpenditureAndActivityWorks"],
    });
  };

  const bulkUploadConfig = {
    entityName: "Asset Expenditure",
    uploadEndpoint: "/api/asset-expenditure-and-activity-works/bulk-upload",
    errorReportEndpoint: "/api/asset-expenditure-and-activity-works/bulk-upload/errors",
    onSuccess: handleBulkUploadSuccess,
  };

  return (
    <>
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
      <AssetExpenditureAndActivityWorkDeleteDialog />
    </>
  );
}
