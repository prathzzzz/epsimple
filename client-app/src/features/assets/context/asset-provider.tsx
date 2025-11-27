import { createContext, useContext, useState, type ReactNode } from 'react'
import type { Asset } from '../api/schema'

interface AssetContextType {
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
  isFinancialDialogOpen: boolean
  setIsFinancialDialogOpen: (open: boolean) => void
  assetForFinancial: Asset | null
  setAssetForFinancial: (asset: Asset | null) => void
}

const AssetContext = createContext<AssetContextType | undefined>(undefined)

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
  const [isFinancialDialogOpen, setIsFinancialDialogOpen] = useState(false)
  const [assetForFinancial, setAssetForFinancial] = useState<Asset | null>(null)

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
        isFinancialDialogOpen,
        setIsFinancialDialogOpen,
        assetForFinancial,
        setAssetForFinancial,
      }}
    >
      {children}
    </AssetContext.Provider>
  )
}

export function useAssetContext() {
  const context = useContext(AssetContext)
  if (!context) {
    throw new Error('useAssetContext must be used within AssetProvider')
  }
  return context
}
