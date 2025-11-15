import { Button } from '@/components/ui/button'
import { PlusCircle } from 'lucide-react'
import { useAsset } from '../hooks/use-asset'

export function CreateAssetButton() {
  const { setIsDrawerOpen, setEditingAsset } = useAsset()

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
