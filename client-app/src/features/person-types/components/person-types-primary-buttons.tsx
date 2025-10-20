import { Plus } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { usePersonTypes } from './person-types-provider'

export function PersonTypesPrimaryButtons() {
  const { setIsDrawerOpen, setSelectedPersonType } = usePersonTypes()

  const handleAddNew = () => {
    setSelectedPersonType(null)
    setIsDrawerOpen(true)
  }

  return (
    <Button onClick={handleAddNew}>
      <Plus className='size-4' />
      Add Person Type
    </Button>
  )
}
