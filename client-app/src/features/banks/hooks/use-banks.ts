import { useContext } from 'react'
import { BanksContext } from '../context/banks-context'
import type { BanksContextType } from '../context/banks-context'

export function useBanks(): BanksContextType {
  const context = useContext(BanksContext)
  if (!context) {
    throw new Error('useBanks must be used within BanksProvider')
  }
  return context
}
