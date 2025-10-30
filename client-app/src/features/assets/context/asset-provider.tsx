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
}

const AssetContext = createContext<AssetContextType | undefined>(undefined)

export function AssetProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false)
  const [editingAsset, setEditingAsset] = useState<Asset | null>(null)
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
  const [assetToDelete, setAssetToDelete] = useState<Asset | null>(null)

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
