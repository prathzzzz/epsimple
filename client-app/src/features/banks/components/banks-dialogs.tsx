import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ConfirmDialog } from '@/components/confirm-dialog'
import { BanksMutateDrawer } from './banks-mutate-drawer'
import { useBanks } from './banks-provider'
import { deleteBank } from '@/lib/banks-api'
import { toast } from 'sonner'

export function BanksDialogs() {
  const { open, setOpen, currentRow, setCurrentRow } = useBanks()
  const queryClient = useQueryClient()

  const deleteMutation = useMutation({
    mutationFn: (id: number) => deleteBank(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['banks'] })
      toast.success('Bank deleted successfully')
      setOpen(null)
      setTimeout(() => {
        setCurrentRow(null)
      }, 500)
    },
    onError: (error: any) => {
      const message = error?.response?.data?.message || 'Failed to delete bank'
      toast.error(message)
    },
  })

  return (
    <>
      <BanksMutateDrawer
        key='bank-create'
        open={open === 'create'}
        onOpenChange={() => setOpen('create')}
      />

      {currentRow && (
        <>
          <BanksMutateDrawer
            key={`bank-update-${currentRow.id}`}
            open={open === 'update'}
            onOpenChange={() => {
              setOpen('update')
              setTimeout(() => {
                setCurrentRow(null)
              }, 500)
            }}
            currentRow={currentRow}
          />

          <ConfirmDialog
            key='bank-delete'
            destructive
            open={open === 'delete'}
            onOpenChange={() => {
              setOpen('delete')
              setTimeout(() => {
                setCurrentRow(null)
              }, 500)
            }}
            handleConfirm={() => {
              deleteMutation.mutate(currentRow.id)
            }}
            className='max-w-md'
            title={`Delete bank: ${currentRow.bankName}?`}
            desc={
              <>
                You are about to delete the bank{' '}
                <strong>{currentRow.bankName}</strong>. <br />
                This action cannot be undone.
              </>
            }
            confirmText='Delete'
          />
        </>
      )}
    </>
  )
}
