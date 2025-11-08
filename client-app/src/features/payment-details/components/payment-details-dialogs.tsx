import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { PaymentDetailsMutateDrawer } from "./payment-details-mutate-drawer";
import { usePaymentDetails } from "../context/payment-details-provider";
import { paymentDetailsApi } from "../api/payment-details-api";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";

export function PaymentDetailsDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedPaymentDetails,
    setSelectedPaymentDetails,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    isBulkUploadDialogOpen,
    closeBulkUploadDialog,
  } = usePaymentDetails();

  const deleteMutation = useMutation({
    mutationFn: paymentDetailsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payment-details"] });
      toast.success("Payment details deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedPaymentDetails(null);
    },
  });

  const handleDelete = () => {
    if (selectedPaymentDetails) {
      deleteMutation.mutate(selectedPaymentDetails.id);
    }
  };

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ["payment-details"] });
    toast.success("Payment details imported successfully");
  };

  return (
    <>
      <PaymentDetailsMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedPaymentDetails}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Payment Details"
        desc={`Are you sure you want to delete this payment record (Amount: ₹${selectedPaymentDetails?.paymentAmount?.toFixed(2)})? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={{
          entityName: "PaymentDetails",
          uploadEndpoint: "/api/payment-details/bulk-upload",
          errorReportEndpoint: "/api/payment-details/export-errors",
          onSuccess: handleBulkUploadSuccess,
        }}
      />
    </>
  );
}
