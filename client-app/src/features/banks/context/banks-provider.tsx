import { useState } from 'react'
import useDialogState from '@/hooks/use-dialog-state'
import { BanksContext, type BanksDialogType } from './banks-context'
import type { Bank } from '../data/schema'

export function BanksProvider({ children }: { children: React.ReactNode }) {
  const [open, setOpen] = useDialogState<BanksDialogType>(null)
  const [currentRow, setCurrentRow] = useState<Bank | null>(null)
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false)

  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true)
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false)

  return (
    <BanksContext.Provider
      value={{
        open,
        setOpen,
        currentRow,
        setCurrentRow,
        isBulkUploadDialogOpen,
        openBulkUploadDialog,
        closeBulkUploadDialog,
      }}
    >
      {children}
    </BanksContext.Provider>
  )
}
