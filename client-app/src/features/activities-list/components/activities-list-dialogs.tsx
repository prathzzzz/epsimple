import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { ActivitiesListMutateDrawer } from "./activities-list-mutate-drawer";
import { useActivitiesList } from "../context/activities-list-provider";
import { activitiesListApi } from "../api/activities-list-api";

export function ActivitiesListDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedActivitiesList,
    setSelectedActivitiesList,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
  } = useActivitiesList();

  const deleteMutation = useMutation({
    mutationFn: activitiesListApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["activities"] });
      toast.success("Activities entry deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedActivitiesList(null);
    },
  });

  const handleDelete = () => {
    if (selectedActivitiesList) {
      deleteMutation.mutate(selectedActivitiesList.id);
    }
  };

  return (
    <>
      <ActivitiesListMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedActivitiesList}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Activities Entry"
        desc={`Are you sure you want to delete the activities entry "${selectedActivitiesList?.activityName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
    </>
  );
}
