import { createContext, useContext, useState, type ReactNode } from 'react'
import type { PersonType } from '../data/schema'

interface PersonTypesContextType {
  selectedPersonType: PersonType | null
  setSelectedPersonType: (personType: PersonType | null) => void
  isDrawerOpen: boolean
  setIsDrawerOpen: (open: boolean) => void
  isDeleteDialogOpen: boolean
  setIsDeleteDialogOpen: (open: boolean) => void
  isBulkUploadDialogOpen: boolean
  setIsBulkUploadDialogOpen: (open: boolean) => void
  openBulkUploadDialog: () => void
  closeBulkUploadDialog: () => void
}

const PersonTypesContext = createContext<PersonTypesContextType | undefined>(
  undefined
)

export function PersonTypesProvider({ children }: { children: ReactNode }) {
  const [selectedPersonType, setSelectedPersonType] =
    useState<PersonType | null>(null)
  const [isDrawerOpen, setIsDrawerOpen] = useState(false)
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false)

  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true)
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false)

  return (
    <PersonTypesContext.Provider
      value={{
        selectedPersonType,
        setSelectedPersonType,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        openBulkUploadDialog,
        closeBulkUploadDialog,
      }}
    >
      {children}
    </PersonTypesContext.Provider>
  )
}

export function usePersonTypes() {
  const context = useContext(PersonTypesContext)
  if (!context) {
    throw new Error('usePersonTypes must be used within PersonTypesProvider')
  }
  return context
}
