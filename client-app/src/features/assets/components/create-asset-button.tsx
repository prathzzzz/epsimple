import { Button } from '@/components/ui/button'
import { Plus } from 'lucide-react'
import { useAssetContext } from '../context/asset-provider'

export function CreateAssetButton() {
  const { setIsDrawerOpen, setEditingAsset } = useAssetContext()

  const handleClick = () => {
    setEditingAsset(null)
    setIsDrawerOpen(true)
  }

  return (
    <Button onClick={handleClick}>
      <Plus className="mr-2 h-4 w-4" />
      Add Asset
    </Button>
  )
}
