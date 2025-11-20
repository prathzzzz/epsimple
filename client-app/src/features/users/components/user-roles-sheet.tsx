import { useState } from 'react'
import { toast } from 'sonner'
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Checkbox } from '@/components/ui/checkbox'
import { Shield, UserCog, Check, X } from 'lucide-react'
import { useRoles } from '@/hooks/use-roles'
import { useAssignRole, useRemoveRole } from '@/hooks/use-user-roles'
import { cn } from '@/lib/utils'
import type { User } from '../data/schema'

interface UserRolesSheetProps {
  user: User | null
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function UserRolesSheet({ user, open, onOpenChange }: UserRolesSheetProps) {
  const { data: availableRoles = [], isLoading } = useRoles()
  const assignRole = useAssignRole()
  const removeRole = useRemoveRole()
  
  const [pendingChanges, setPendingChanges] = useState<{
    toAdd: number[]
    toRemove: number[]
  }>({ toAdd: [], toRemove: [] })

  if (!user) return null

  // Support both mock data (user.role as string) and API data (user.roles as RoleDTO[])
  const userRoleNames = user.roles 
    ? user.roles.map(r => r.name) 
    : user.role 
      ? [user.role] 
      : []
  
  const isRoleAssigned = (roleName: string) => {
    const role = availableRoles.find(r => r.name === roleName)
    if (!role) return false
    
    const isCurrentlyAssigned = userRoleNames.some((name: string) => name === roleName)
    const isPendingAdd = pendingChanges.toAdd.includes(role.id)
    const isPendingRemove = pendingChanges.toRemove.includes(role.id)
    
    return (isCurrentlyAssigned && !isPendingRemove) || isPendingAdd
  }

  const toggleRole = (roleId: number, roleName: string) => {
    const isCurrentlyAssigned = userRoleNames.some((name: string) => name === roleName)
    
    setPendingChanges(prev => {
      const newChanges = { ...prev }
      
      if (isCurrentlyAssigned) {
        // Currently assigned - toggle removal
        if (prev.toRemove.includes(roleId)) {
          newChanges.toRemove = prev.toRemove.filter(id => id !== roleId)
        } else {
          newChanges.toRemove = [...prev.toRemove, roleId]
        }
      } else {
        // Not assigned - toggle addition
        if (prev.toAdd.includes(roleId)) {
          newChanges.toAdd = prev.toAdd.filter(id => id !== roleId)
        } else {
          newChanges.toAdd = [...prev.toAdd, roleId]
        }
      }
      
      return newChanges
    })
  }

  const handleSave = async () => {
    const userId = typeof user.id === 'string' ? parseInt(user.id) : user.id
    
    try {
      // Remove roles first
      for (const roleId of pendingChanges.toRemove) {
        await removeRole.mutateAsync({ userId, roleId })
      }
      
      // Then add new roles
      for (const roleId of pendingChanges.toAdd) {
        await assignRole.mutateAsync({ userId, roleId })
      }
      
      setPendingChanges({ toAdd: [], toRemove: [] })
      onOpenChange(false)
      toast.success('Roles updated successfully')
    } catch {
      // Errors handled by mutations
    }
  }

  const handleCancel = () => {
    setPendingChanges({ toAdd: [], toRemove: [] })
    onOpenChange(false)
  }

  const hasChanges = pendingChanges.toAdd.length > 0 || pendingChanges.toRemove.length > 0
  const isSaving = assignRole.isPending || removeRole.isPending

  return (
    <Sheet open={open} onOpenChange={handleCancel}>
      <SheetContent className='flex flex-col sm:max-w-lg'>
        <SheetHeader className='text-start'>
          <div className='flex items-center gap-2'>
            <div className='flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10'>
              <UserCog className='h-5 w-5 text-primary' />
            </div>
            <div>
              <SheetTitle>Manage User Roles</SheetTitle>
              <SheetDescription>
                Assign or remove roles for {user.username}
              </SheetDescription>
            </div>
          </div>
        </SheetHeader>

        <div className='flex-1 space-y-4 overflow-y-auto px-4'>
          {/* Current User Info */}
          <div className='rounded-lg border bg-muted/50 p-3'>
            <div className='text-sm font-medium'>{user.firstName} {user.lastName}</div>
            <div className='text-xs text-muted-foreground'>{user.email}</div>
          </div>

          {/* Roles List */}
          <div>
            <h4 className='mb-3 text-sm font-medium'>Available Roles</h4>
            {isLoading ? (
              <div className='flex items-center justify-center p-8'>
                <div className='text-sm text-muted-foreground'>Loading roles...</div>
              </div>
            ) : (
              <ScrollArea className='h-[400px] rounded-md border'>
                <div className='p-3 space-y-2'>
                  {availableRoles.map((role) => {
                    const isAssigned = isRoleAssigned(role.name)
                    const isPendingAdd = pendingChanges.toAdd.includes(role.id)
                    const isPendingRemove = pendingChanges.toRemove.includes(role.id)
                    
                    return (
                      <div
                        key={role.id}
                        className={cn(
                          'flex items-start gap-3 rounded-lg border p-3 transition-colors cursor-pointer',
                          'hover:bg-accent',
                          isAssigned && 'bg-accent/50',
                          (isPendingAdd || isPendingRemove) && 'ring-2 ring-primary'
                        )}
                        onClick={() => toggleRole(role.id, role.name)}
                      >
                        <Checkbox
                          checked={isAssigned}
                          className='mt-0.5 pointer-events-none'
                        />
                        <div className='flex-1 min-w-0'>
                          <div className='flex items-center gap-2 mb-1'>
                            <Shield className='h-4 w-4 text-muted-foreground shrink-0' />
                            <span className='font-medium text-sm'>{role.name}</span>
                            {role.isSystemRole && (
                              <Badge variant='outline' className='text-xs'>System</Badge>
                            )}
                            {isPendingAdd && (
                              <Badge className='text-xs bg-green-500'>
                                <Check className='h-3 w-3 mr-1' />
                                Will Add
                              </Badge>
                            )}
                            {isPendingRemove && (
                              <Badge variant='destructive' className='text-xs'>
                                <X className='h-3 w-3 mr-1' />
                                Will Remove
                              </Badge>
                            )}
                          </div>
                          {role.description && (
                            <p className='text-xs text-muted-foreground line-clamp-2'>
                              {role.description}
                            </p>
                          )}
                          <div className='mt-2 flex items-center gap-2'>
                            <Badge variant='secondary' className='text-xs'>
                              {role.permissions?.length || 0} permissions
                            </Badge>
                            <Badge
                              variant='outline'
                              className={cn(
                                'text-xs',
                                role.isActive
                                  ? 'border-green-500/50 bg-green-500/10 text-green-700 dark:text-green-400'
                                  : 'border-gray-500/50 bg-gray-500/10 text-gray-700 dark:text-gray-400'
                              )}
                            >
                              {role.isActive ? 'Active' : 'Inactive'}
                            </Badge>
                          </div>
                        </div>
                      </div>
                    )
                  })}
                </div>
              </ScrollArea>
            )}
          </div>
        </div>

        <SheetFooter className='gap-2'>
          <Button
            type='button'
            variant='outline'
            onClick={handleCancel}
            disabled={isSaving}
          >
            Cancel
          </Button>
          <Button
            type='button'
            onClick={handleSave}
            disabled={!hasChanges || isSaving}
          >
            {isSaving ? 'Saving...' : 'Save Changes'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  )
}
