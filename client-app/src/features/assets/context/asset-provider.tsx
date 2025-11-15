import { useState, type ReactNode } from 'react'
import { AssetContext } from './asset-context'
import type { Asset } from '../api/schema'

export function AssetProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false)
  const [editingAsset, setEditingAsset] = useState<Asset | null>(null)
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
  const [assetToDelete, setAssetToDelete] = useState<Asset | null>(null)
  const [isMovementDialogOpen, setIsMovementDialogOpen] = useState(false)
  const [assetForMovement, setAssetForMovement] = useState<Asset | null>(null)
  const [isPlacementDialogOpen, setIsPlacementDialogOpen] = useState(false)
  const [assetForPlacement, setAssetForPlacement] = useState<Asset | null>(null)
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false)
  const [isPlacementBulkUploadDialogOpen, setIsPlacementBulkUploadDialogOpen] = useState(false)

  return (
    <AssetContext.Provider
      value={{
        isDrawerOpen,
        setIsDrawerOpen,
        editingAsset,
        setEditingAsset,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        assetToDelete,
        setAssetToDelete,
        isMovementDialogOpen,
        setIsMovementDialogOpen,
        assetForMovement,
        setAssetForMovement,
        isPlacementDialogOpen,
        setIsPlacementDialogOpen,
        assetForPlacement,
        setAssetForPlacement,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        isPlacementBulkUploadDialogOpen,
        setIsPlacementBulkUploadDialogOpen,
      }}
    >
      {children}
    </AssetContext.Provider>
  )
}
