import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { InvoiceMutateDrawer } from "./invoice-mutate-drawer";
import { useInvoice } from "../context/invoice-provider";
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
    </>
  );
}
