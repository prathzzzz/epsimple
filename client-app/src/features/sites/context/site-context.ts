import { createContext } from 'react'
import type { Site } from '../api/schema'

export interface SiteContextType {
  isDrawerOpen: boolean
  setIsDrawerOpen: (open: boolean) => void
  isDeleteDialogOpen: boolean
  setIsDeleteDialogOpen: (open: boolean) => void
  editingSite: Site | null
  setEditingSite: (site: Site | null) => void
  deletingSiteId: number | null
  setDeletingSiteId: (id: number | null) => void
  globalFilter: string
  setGlobalFilter: (filter: string) => void
  isBulkUploadDialogOpen: boolean
  setIsBulkUploadDialogOpen: (open: boolean) => void
}

export const SiteContext = createContext<SiteContextType | undefined>(undefined)
