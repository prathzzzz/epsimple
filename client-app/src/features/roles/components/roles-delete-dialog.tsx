import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog'
import { useDeleteRole } from '@/hooks/use-roles'
import type { RoleDTO } from '@/lib/roles-api'

interface RolesDeleteDialogProps {
  role: RoleDTO | null
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function RolesDeleteDialog({
  role,
  open,
  onOpenChange,
}: RolesDeleteDialogProps) {
  const deleteRole = useDeleteRole()

  const handleDelete = async () => {
    if (!role) return
    
    try {
      await deleteRole.mutateAsync(role.id)
      onOpenChange(false)
    } catch {
      // Error handled by mutation hook
    }
  }

  if (!role) return null

  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This will permanently delete the role <strong className="font-semibold text-foreground">"{role.name}"</strong>.
            {role.permissions && role.permissions.length > 0 && (
              <span className="block mt-2">
                This role has {role.permissions.length} permission{role.permissions.length !== 1 ? 's' : ''} assigned.
              </span>
            )}
            This action cannot be undone.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel disabled={deleteRole.isPending}>
            Cancel
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete}
            disabled={deleteRole.isPending}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            {deleteRole.isPending ? 'Deleting...' : 'Delete Role'}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}
