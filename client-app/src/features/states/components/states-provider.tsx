import { createContext, useContext, useState, type ReactNode } from 'react'
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

const StatesContext = createContext<StatesContextType | undefined>(undefined)

export function StatesProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false)
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false)
  const [selectedState, setSelectedState] = useState<State | null>(null)
  const [isEditMode, setIsEditMode] = useState(false)

  const openDrawer = () => setIsDrawerOpen(true)
  const closeDrawer = () => {
    setIsDrawerOpen(false)
    setSelectedState(null)
    setIsEditMode(false)
  }

  const openDeleteDialog = () => setIsDeleteDialogOpen(true)
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false)
    setSelectedState(null)
  }
  
  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true)
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false)

  return (
    <StatesContext.Provider
      value={{
        isDrawerOpen,
        openDrawer,
        closeDrawer,
        isDeleteDialogOpen,
        openDeleteDialog,
        closeDeleteDialog,
        isBulkUploadDialogOpen,
        openBulkUploadDialog,
        closeBulkUploadDialog,
        selectedState,
        setSelectedState,
        isEditMode,
        setIsEditMode,
      }}
    >
      {children}
    </StatesContext.Provider>
  )
}

// eslint-disable-next-line react-refresh/only-export-components
export function useStates() {
  const context = useContext(StatesContext)
  if (context === undefined) {
    throw new Error('useStates must be used within a StatesProvider')
  }
  return context
}
