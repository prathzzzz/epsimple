import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { Loader2 } from "lucide-react";

import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";

import { vouchersApi } from "../api/vouchers-api";
import { useVoucher } from "../hooks/use-voucher";

export function VoucherDialogs() {
  const queryClient = useQueryClient();
  const {
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    voucherToDelete,
    setVoucherToDelete,
    isBulkUploadDialogOpen,
    setIsBulkUploadDialogOpen,
  } = useVoucher();

  const deleteMutation = useMutation({
    mutationFn: vouchersApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["vouchers"] });
      toast.success("Voucher deleted successfully");
      setIsDeleteDialogOpen(false);
      setVoucherToDelete(null);
    },
    onError: () => {
      toast.error("Failed to delete voucher");
    },
  });

  const handleDelete = () => {
    if (voucherToDelete) {
      deleteMutation.mutate(voucherToDelete.id);
    }
  };

  return (
    <>
      <AlertDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone. This will permanently delete the
              voucher
              {voucherToDelete && ` "${voucherToDelete.voucherNumber}"`}.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel disabled={deleteMutation.isPending}>
              Cancel
            </AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDelete}
              disabled={deleteMutation.isPending}
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            >
              {deleteMutation.isPending ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Deleting...
                </>
              ) : (
                "Delete"
              )}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          entityName: "Voucher",
          uploadEndpoint: "/api/vouchers/bulk-upload",
          errorReportEndpoint: "/api/vouchers/bulk-upload/errors",
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["vouchers"] });
          },
        }}
      />
    </>
  );
}
