import { useContext } from 'react'
import { DatacenterContext } from '../context/datacenter-context'
import type { DatacenterContextType } from '../context/datacenter-context'

export function useDatacenter(): DatacenterContextType {
  const context = useContext(DatacenterContext)
  if (!context) {
    throw new Error('useDatacenter must be used within DatacenterProvider')
  }
  return context
}
