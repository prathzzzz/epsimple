import { createContext } from 'react'
import type { Datacenter } from '../api/schema'

export interface DatacenterContextType {
  selectedDatacenter: Datacenter | null
  setSelectedDatacenter: (datacenter: Datacenter | null) => void
  isDrawerOpen: boolean
  setIsDrawerOpen: (show: boolean) => void
  isDeleteDialogOpen: boolean
  setIsDeleteDialogOpen: (show: boolean) => void
  isBulkUploadDialogOpen: boolean
  globalFilter: string
  setGlobalFilter: (filter: string) => void
  openDrawer: () => void
  closeDrawer: () => void
  openDeleteDialog: () => void
  closeDeleteDialog: () => void
  openBulkUploadDialog: () => void
  closeBulkUploadDialog: () => void
}

export const DatacenterContext = createContext<DatacenterContextType | undefined>(undefined)
