import React, { useState } from 'react'
import useDialogState from '@/hooks/use-dialog-state'
import { type Bank } from '../data/schema'

type BanksDialogType = 'create' | 'update' | 'delete'

type BanksContextType = {
  open: BanksDialogType | null
  setOpen: (str: BanksDialogType | null) => void
  currentRow: Bank | null
  setCurrentRow: React.Dispatch<React.SetStateAction<Bank | null>>
}

const BanksContext = React.createContext<BanksContextType | null>(null)

export function BanksProvider({ children }: { children: React.ReactNode }) {
  const [open, setOpen] = useDialogState<BanksDialogType>(null)
  const [currentRow, setCurrentRow] = useState<Bank | null>(null)

  return (
    <BanksContext value={{ open, setOpen, currentRow, setCurrentRow }}>
      {children}
    </BanksContext>
  )
}

// eslint-disable-next-line react-refresh/only-export-components
export const useBanks = () => {
  const banksContext = React.useContext(BanksContext)

  if (!banksContext) {
    throw new Error('useBanks has to be used within <BanksProvider>')
  }

  return banksContext
}
