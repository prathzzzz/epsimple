import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { InvoiceMutateDrawer } from "./invoice-mutate-drawer";
import { useInvoice } from "../hooks/use-invoice";
import { invoicesApi } from "../api/invoices-api";

export function InvoiceDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedInvoice,
    setSelectedInvoice,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    isBulkUploadDialogOpen,
    setIsBulkUploadDialogOpen,
  } = useInvoice();

  const deleteMutation = useMutation({
    mutationFn: invoicesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["invoices"] });
      toast.success("Invoice deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedInvoice(null);
    },
  });

  const handleDelete = () => {
    if (selectedInvoice) {
      deleteMutation.mutate(selectedInvoice.id);
    }
  };

  return (
    <>
      <InvoiceMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedInvoice}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Invoice"
        desc={`Are you sure you want to delete invoice "${selectedInvoice?.invoiceNumber}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          entityName: "Invoice",
          uploadEndpoint: "/api/invoices/bulk-upload",
          errorReportEndpoint: "/api/invoices/bulk-upload/errors",
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["invoices"] });
            toast.success("Invoice bulk upload completed successfully");
          },
        }}
      />
    </>
  );
}
