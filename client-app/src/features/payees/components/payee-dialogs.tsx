import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { usePayee } from "../context/payee-provider";
import { PayeeDeleteDialog } from "./payee-delete-dialog";
import { PayeeDrawer } from "./payee-drawer";
import { useQueryClient } from "@tanstack/react-query";

export function PayeeDialogs() {
  const queryClient = useQueryClient();
  const { isBulkUploadDialogOpen, setIsBulkUploadDialogOpen } = usePayee();

  return (
    <>
      <PayeeDrawer />
      <PayeeDeleteDialog />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          entityName: "Payee",
          uploadEndpoint: "/api/payees/bulk-upload",
          errorReportEndpoint: "/api/payees/export-errors",
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["payees"] });
          },
        }}
      />
    </>
  );
}
