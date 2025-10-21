import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { ConfirmDialog } from '@/components/confirm-dialog';
import { MovementTypesMutateDrawer } from './movement-types-mutate-drawer';
import { useMovementTypes } from '../context/movement-types-provider';
import { movementTypesApi } from '../api/movement-types-api';

export function MovementTypesDialogs() {
  const queryClient = useQueryClient();
  const {
    selectedMovementType,
    setSelectedMovementType,
    isDrawerOpen,
    setIsDrawerOpen,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
  } = useMovementTypes();

  const deleteMutation = useMutation({
    mutationFn: movementTypesApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['movement-types'] });
      toast.success('Movement type deleted successfully');
      setIsDeleteDialogOpen(false);
      setSelectedMovementType(null);
    },
  });

  const handleDelete = () => {
    if (selectedMovementType) {
      deleteMutation.mutate(selectedMovementType.id);
    }
  };

  return (
    <>
      <MovementTypesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedMovementType}
      />
      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        title="Delete Movement Type"
        desc={`Are you sure you want to delete the movement type "${selectedMovementType?.movementType}"? This action cannot be undone.`}
        handleConfirm={handleDelete}
        isLoading={deleteMutation.isPending}
        destructive
      />
    </>
  );
}
