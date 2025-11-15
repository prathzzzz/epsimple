import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ConfirmDialog } from '@/components/confirm-dialog'
import { StatesMutateDrawer } from './states-mutate-drawer'
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog'
import { useStates } from '../hooks/use-states'
import { statesApi } from '@/features/states/api/states-api'
import { toast } from 'sonner'
import { handleServerError } from '@/lib/handle-server-error'

export function StatesDialogs() {
  const { 
    isDrawerOpen, 
    closeDrawer, 
    isDeleteDialogOpen, 
    closeDeleteDialog, 
    isBulkUploadDialogOpen,
    closeBulkUploadDialog,
    selectedState, 
    isEditMode 
  } = useStates()
  const queryClient = useQueryClient()

  const deleteMutation = useMutation({
    mutationFn: (id: number) => statesApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['states'] })
      toast.success('State deleted successfully')
      closeDeleteDialog()
    },
    onError: (error: unknown) => {
      handleServerError(error)
    },
  })

  const handleBulkUploadSuccess = () => {
    queryClient.invalidateQueries({ queryKey: ['states'] })
  }

  const bulkUploadConfig = {
    entityName: 'State',
    uploadEndpoint: '/api/states/bulk-upload',
    errorReportEndpoint: '/api/states/export-errors',
    onSuccess: handleBulkUploadSuccess,
  }

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
      
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={closeBulkUploadDialog}
        config={bulkUploadConfig}
      />
    </>
  )
}
