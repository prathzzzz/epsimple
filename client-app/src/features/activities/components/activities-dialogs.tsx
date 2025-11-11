import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { ActivitiesMutateDrawer } from "./activities-mutate-drawer";
import { useActivities } from "../context/activities-provider";
import { activitiesApi } from "../api/activities-api";

export function ActivitiesDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedActivity,
    setSelectedActivity,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    isBulkUploadDialogOpen,
    setIsBulkUploadDialogOpen,
  } = useActivities();

  const deleteMutation = useMutation({
    mutationFn: activitiesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["activity"] });
      toast.success("Activity deleted successfully");
      setIsDeleteDialogOpen(false);
      setSelectedActivity(null);
    },
  });

  const handleDelete = () => {
    if (selectedActivity) {
      deleteMutation.mutate(selectedActivity.id);
    }
  };

  return (
    <>
      <ActivitiesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedActivity}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Activity"
        desc={`Are you sure you want to delete the activity "${selectedActivity?.activityName}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          uploadEndpoint: '/api/activity/bulk-upload',
          errorReportEndpoint: '/api/activity/bulk-upload/errors',
          entityName: 'Activity',
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['activity'] });
          },
        }}
      />
    </>
  );
}
