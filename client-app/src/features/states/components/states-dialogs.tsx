import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ConfirmDialog } from '@/components/confirm-dialog'
import { StatesMutateDrawer } from './states-mutate-drawer'
import { useStates } from './states-provider'
import { statesApi } from '@/lib/states-api'
import { toast } from 'sonner'

export function StatesDialogs() {
  const { isDrawerOpen, closeDrawer, isDeleteDialogOpen, closeDeleteDialog, selectedState, isEditMode } = useStates()
  const queryClient = useQueryClient()

  const deleteMutation = useMutation({
    mutationFn: (id: number) => statesApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['states'] })
      toast.success('State deleted successfully')
      closeDeleteDialog()
    },
    onError: (error: any) => {
      const message = error?.response?.data?.message || 'Failed to delete state'
      toast.error(message)
    },
  })

  return (
    <>
      <StatesMutateDrawer
        key={isEditMode && selectedState ? `state-update-${selectedState.id}` : 'state-create'}
        open={isDrawerOpen}
        onOpenChange={closeDrawer}
        currentRow={isEditMode && selectedState ? selectedState : undefined}
      />

      {selectedState && (
        <ConfirmDialog
          key='state-delete'
          destructive
          open={isDeleteDialogOpen}
          onOpenChange={closeDeleteDialog}
          handleConfirm={() => {
            deleteMutation.mutate(selectedState.id)
          }}
          className='max-w-md'
          title={`Delete state: ${selectedState.stateName}?`}
          desc={
            <>
              You are about to delete the state{' '}
              <strong>{selectedState.stateName}</strong>. <br />
              This action cannot be undone.
            </>
          }
          confirmText='Delete'
        />
      )}
    </>
  )
}
