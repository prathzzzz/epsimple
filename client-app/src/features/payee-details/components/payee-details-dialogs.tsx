import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { usePayeeDetails } from "../context/payee-details-provider";
import { PayeeDetailsDeleteDialog } from "./payee-details-delete-dialog";
import { PayeeDetailsDrawer } from "./payee-details-drawer";
import { useQueryClient } from "@tanstack/react-query";

export function PayeeDetailsDialogs() {
  const queryClient = useQueryClient();
  const {
    isBulkUploadDialogOpen,
    setIsBulkUploadDialogOpen,
  } = usePayeeDetails();

  return (
    <>
      <PayeeDetailsDrawer />
      <PayeeDetailsDeleteDialog />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          entityName: "Payee Details",
          uploadEndpoint: "/api/payee-details/bulk-upload",
          errorReportEndpoint: "/api/payee-details/export-errors",
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["payee-details"] });
          },
        }}
      />
    </>
  );
}
