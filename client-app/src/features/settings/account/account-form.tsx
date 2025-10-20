import { useAuthStore } from '@/stores/auth-store'
import { Badge } from '@/components/ui/badge'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'

export function AccountForm() {
  const { user } = useAuthStore()

  if (!user) {
    return <div>Loading...</div>
  }

  // Helper function to format role names - capitalize first letter, rest lowercase
  const formatRoleName = (name: string) => {
    return name.charAt(0).toUpperCase() + name.slice(1).toLowerCase()
  }

  // Helper function to format permission names - capitalize first letter, rest lowercase  
  const formatPermissionName = (name: string) => {
    return name.charAt(0).toUpperCase() + name.slice(1).toLowerCase()
  }

  return (
    <div className='space-y-6'>
      <Card>
        <CardHeader>
          <CardTitle>Account Information</CardTitle>
          <CardDescription>
            View your account details and current role permissions.
          </CardDescription>
        </CardHeader>
        <CardContent className='space-y-4'>
          <div className='grid grid-cols-2 gap-4'>
            <div>
              <label className='text-sm font-medium text-muted-foreground'>
                User ID
              </label>
              <p className='text-sm'>{user.id}</p>
            </div>
            <div>
              <label className='text-sm font-medium text-muted-foreground'>
                Status
              </label>
              <div>
                <Badge variant={user.isActive ? 'default' : 'destructive'}>
                  {user.isActive ? 'Active' : 'Inactive'}
                </Badge>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Roles & Permissions</CardTitle>
          <CardDescription>
            Your current roles and associated permissions.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className='space-y-4'>
            {user.roles.map((role) => (
              <div key={role.id} className='border rounded-lg p-4'>
                <div className='flex items-center justify-between mb-2'>
                  <h3 className='font-semibold'>{formatRoleName(role.name)}</h3>
                  <Badge variant='outline'>Role</Badge>
                </div>
                <p className='text-sm text-muted-foreground mb-3'>
                  {role.description}
                </p>
                <div>
                  <label className='text-sm font-medium mb-2 block'>
                    Permissions:
                  </label>
                  <div className='flex flex-wrap gap-2'>
                    {role.permissions.map((permission) => (
                      <Badge key={permission.id} variant='secondary'>
                        {formatPermissionName(permission.name)}
                      </Badge>
                    ))}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
