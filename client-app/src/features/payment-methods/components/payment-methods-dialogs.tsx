import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { PaymentMethodsMutateDrawer } from "./payment-methods-mutate-drawer";
import { usePaymentMethods } from "../context/payment-methods-provider";
import { paymentMethodsApi } from "../api/payment-methods-api";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";

export function PaymentMethodsDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedPaymentMethod,
    setSelectedPaymentMethod,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    isBulkUploadDialogOpen,
    closeBulkUploadDialog,
  } = usePaymentMethods();

  const deleteMutation = useMutation({
    mutationFn: paymentMethodsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payment-methods"] });
      toast.success("Payment method deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedPaymentMethod(null);
    },
  });

  const handleDelete = () => {
    if (selectedPaymentMethod) {
      deleteMutation.mutate(selectedPaymentMethod.id);
    }
  };

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ["payment-methods"] });
    toast.success("Payment methods imported successfully");
  };

  return (
    <>
      <PaymentMethodsMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedPaymentMethod}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Payment Method"
        desc={`Are you sure you want to delete the payment method "${selectedPaymentMethod?.methodName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={{
          entityName: "PaymentMethod",
          uploadEndpoint: "/api/payment-methods/bulk-upload",
          errorReportEndpoint: "/api/payment-methods/export-errors",
          onSuccess: handleBulkUploadSuccess,
        }}
      />
    </>
  );
}
