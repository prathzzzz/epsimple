import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { GenericStatusTypeMutateDrawer } from "./generic-status-type-mutate-drawer";
import { useGenericStatusType } from "../context/generic-status-type-provider";
import { genericStatusTypeApi } from "../api/generic-status-type-api";

export function GenericStatusTypeDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedStatusType,
    setSelectedStatusType,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
  } = useGenericStatusType();

  const deleteMutation = useMutation({
    mutationFn: genericStatusTypeApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["generic-status-types"] });
      toast.success("Generic status type deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedStatusType(null);
    },
  });

  const handleDelete = () => {
    if (selectedStatusType) {
      deleteMutation.mutate(selectedStatusType.id);
    }
  };

  return (
    <>
      <GenericStatusTypeMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedStatusType}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Generic Status Type"
        desc={`Are you sure you want to delete the status type "${selectedStatusType?.statusName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
    </>
  );
}
