import { createContext } from 'react'
import type { State } from '../data/schema'

export interface StatesContextType {
  // Drawer state
  isDrawerOpen: boolean
  openDrawer: () => void
  closeDrawer: () => void

  // Delete dialog state
  isDeleteDialogOpen: boolean
  openDeleteDialog: () => void
  closeDeleteDialog: () => void
  
  // Bulk upload dialog state
  isBulkUploadDialogOpen: boolean
  openBulkUploadDialog: () => void
  closeBulkUploadDialog: () => void

  // Selected state for edit/delete
  selectedState: State | null
  setSelectedState: (state: State | null) => void

  // Edit mode
  isEditMode: boolean
  setIsEditMode: (isEdit: boolean) => void
}

export const StatesContext = createContext<StatesContextType | undefined>(undefined)
