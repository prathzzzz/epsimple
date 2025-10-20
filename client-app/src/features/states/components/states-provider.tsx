import { createContext, useContext, useState, type ReactNode } from 'react'
import type { State } from '../data/schema'

interface StatesContextType {
  // Drawer state
  isDrawerOpen: boolean
  openDrawer: () => void
  closeDrawer: () => void

  // Delete dialog state
  isDeleteDialogOpen: boolean
  openDeleteDialog: () => void
  closeDeleteDialog: () => void

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

  return (
    <StatesContext.Provider
      value={{
        isDrawerOpen,
        openDrawer,
        closeDrawer,
        isDeleteDialogOpen,
        openDeleteDialog,
        closeDeleteDialog,
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

export function useStates() {
  const context = useContext(StatesContext)
  if (context === undefined) {
    throw new Error('useStates must be used within a StatesProvider')
  }
  return context
}
