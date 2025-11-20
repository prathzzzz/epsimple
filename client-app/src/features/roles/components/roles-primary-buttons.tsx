import { Plus } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { AdminGuard } from '@/components/admin-guard'
import { useRoles } from './roles-provider'

export function RolesPrimaryButtons() {
  const { setOpen } = useRoles()
  
  return (
    <AdminGuard>
      <Button className='space-x-1' onClick={() => setOpen('add')}>
        <Plus className="mr-2 h-4 w-4" />
        Create Role
      </Button>
    </AdminGuard>
  )
}
