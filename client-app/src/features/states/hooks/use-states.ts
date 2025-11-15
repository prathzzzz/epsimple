import { useContext } from 'react'
import { StatesContext } from '../context/states-context'
import type { StatesContextType } from '../context/states-context'

export function useStates(): StatesContextType {
  const context = useContext(StatesContext)
  if (context === undefined) {
    throw new Error('useStates must be used within StatesProvider')
  }
  return context
}
