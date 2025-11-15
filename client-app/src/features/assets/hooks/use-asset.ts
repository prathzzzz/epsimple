import { useContext } from 'react'
import { AssetContext } from '../context/asset-context'
import type { AssetContextType } from '../context/asset-context'

export function useAsset(): AssetContextType {
  const context = useContext(AssetContext)
  if (context === undefined) {
    throw new Error('useAsset must be used within AssetProvider')
  }
  return context
}
