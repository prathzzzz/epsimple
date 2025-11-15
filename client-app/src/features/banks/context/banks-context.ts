import { createContext } from 'react'
import type { Bank } from '../data/schema'

export type BanksDialogType = 'create' | 'update' | 'delete'

export interface BanksContextType {
  open: BanksDialogType | null
  setOpen: (str: BanksDialogType | null) => void
  currentRow: Bank | null
  setCurrentRow: (bank: Bank | null) => void
  isBulkUploadDialogOpen: boolean
  openBulkUploadDialog: () => void
  closeBulkUploadDialog: () => void
}

export const BanksContext = createContext<BanksContextType | null>(null)
