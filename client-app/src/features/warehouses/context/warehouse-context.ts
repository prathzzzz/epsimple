import { createContext } from 'react'
import type { Warehouse } from '../api/schema'

export interface WarehouseContextType {
  selectedWarehouse: Warehouse | null
  setSelectedWarehouse: (warehouse: Warehouse | null) => void
  isDrawerOpen: boolean
  setIsDrawerOpen: (show: boolean) => void
  isDeleteDialogOpen: boolean
  setIsDeleteDialogOpen: (show: boolean) => void
  isBulkUploadDialogOpen: boolean
  setIsBulkUploadDialogOpen: (show: boolean) => void
  globalFilter: string
  setGlobalFilter: (filter: string) => void
  openDrawer: () => void
  closeDrawer: () => void
  openDeleteDialog: () => void
  closeDeleteDialog: () => void
  openBulkUploadDialog: () => void
  closeBulkUploadDialog: () => void
}

export const WarehouseContext = createContext<WarehouseContextType | undefined>(undefined)
