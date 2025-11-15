import { createContext } from 'react'
import type { Asset } from '../api/schema'

export interface AssetContextType {
  isDrawerOpen: boolean
  setIsDrawerOpen: (open: boolean) => void
  editingAsset: Asset | null
  setEditingAsset: (asset: Asset | null) => void
  isDeleteDialogOpen: boolean
  setIsDeleteDialogOpen: (open: boolean) => void
  assetToDelete: Asset | null
  setAssetToDelete: (asset: Asset | null) => void
  isMovementDialogOpen: boolean
  setIsMovementDialogOpen: (open: boolean) => void
  assetForMovement: Asset | null
  setAssetForMovement: (asset: Asset | null) => void
  isPlacementDialogOpen: boolean
  setIsPlacementDialogOpen: (open: boolean) => void
  assetForPlacement: Asset | null
  setAssetForPlacement: (asset: Asset | null) => void
  isBulkUploadDialogOpen: boolean
  setIsBulkUploadDialogOpen: (open: boolean) => void
  isPlacementBulkUploadDialogOpen: boolean
  setIsPlacementBulkUploadDialogOpen: (open: boolean) => void
}

export const AssetContext = createContext<AssetContextType | undefined>(undefined)
