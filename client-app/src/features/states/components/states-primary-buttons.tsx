import { Button } from '@/components/ui/button'
import { Plus } from 'lucide-react'
import { useStates } from './states-provider'

export function StatesPrimaryButtons() {
  const { openDrawer, setIsEditMode } = useStates()

  const handleCreateClick = () => {
    setIsEditMode(false)
    openDrawer()
  }

  return (
    <Button onClick={handleCreateClick} size='sm'>
      <Plus className='mr-2 h-4 w-4' />
      Add State
    </Button>
  )
}
