import { useMutation, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import { ConfirmDialog } from '@/components/confirm-dialog'
import { personTypesApi } from '@/lib/person-types-api'
import { usePersonTypes } from './person-types-provider'
import { PersonTypesMutateDrawer } from './person-types-mutate-drawer'

export function PersonTypesDialogs() {
  const queryClient = useQueryClient()
  const {
    selectedPersonType,
    isDeleteDialogOpen,
    setIsDeleteDialogOpen,
    isDrawerOpen,
    setIsDrawerOpen,
  } = usePersonTypes()

  const deleteMutation = useMutation({
    mutationFn: (id: number) => personTypesApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['person-types'] })
      toast.success('Person type deleted successfully')
      setIsDeleteDialogOpen(false)
    },
    onError: (error: any) => {
      const message = error?.message || 'Failed to delete person type'
      toast.error(message)
    },
  })

  return (
    <>
      <PersonTypesMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={selectedPersonType || undefined}
      />

      {selectedPersonType && (
        <ConfirmDialog
          key='person-type-delete'
          destructive
          open={isDeleteDialogOpen}
          onOpenChange={setIsDeleteDialogOpen}
          handleConfirm={() => {
            deleteMutation.mutate(selectedPersonType.id)
          }}
          className='max-w-md'
          title={`Delete person type: ${selectedPersonType.typeName}?`}
          desc={
            <>
              You are about to delete the person type{' '}
              <strong>{selectedPersonType.typeName}</strong>. <br />
              This action cannot be undone.
            </>
          }
          confirmText='Delete'
        />
      )}
    </>
  )
}
