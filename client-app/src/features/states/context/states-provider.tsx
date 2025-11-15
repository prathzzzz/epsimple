import { useState, type ReactNode } from 'react'
import { StatesContext } from './states-context'
import type { State } from '../data/schema'

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
