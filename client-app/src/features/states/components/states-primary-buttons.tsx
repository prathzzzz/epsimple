import { Button } from '@/components/ui/button'
import { Plus, Upload } from 'lucide-react'
import { useStates } from './states-provider'

export function StatesPrimaryButtons() {
  const { openDrawer, setIsEditMode, openBulkUploadDialog } = useStates()

  const handleCreateClick = () => {
    setIsEditMode(false)
    openDrawer()
  }

  return (
    <div className="flex gap-2">
      <Button onClick={openBulkUploadDialog} size='sm' variant="outline">
        <Upload className='mr-2 h-4 w-4' />
        Bulk Upload
      </Button>
      <Button onClick={handleCreateClick} size='sm'>
        <Plus className='mr-2 h-4 w-4' />
        Add State
      </Button>
    </div>
  )
}
