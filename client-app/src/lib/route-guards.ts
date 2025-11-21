import { redirect } from '@tanstack/react-router'
import { useAuthStore } from '@/stores/auth-store'

interface PermissionGuardOptions {
  /** Single permission required */
  permission?: string
  /** Any of these permissions (OR logic) */
  anyPermissions?: string[]
  /** All of these permissions (AND logic) */
  allPermissions?: string[]
  /** Require admin access */
  requireAdmin?: boolean
}

/**
 * Route guard that checks if user has required permissions
 * Throws a redirect to /errors/forbidden if user lacks permissions
 * 
 * Usage in route beforeLoad:
 * beforeLoad: () => {
 *   requirePermission({ permission: 'ASSET:READ' })
 * }
 */
export function requirePermission(options: PermissionGuardOptions) {
  const { user } = useAuthStore.getState()

  if (!user) {
    throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
  }

  const userPermissions = user.allPermissions || []
  const isUserAdmin = userPermissions.includes('ALL')

  // Admin has access to everything
  if (isUserAdmin) {
    return
  }

  // Check admin requirement
  if (options.requireAdmin) {
    throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
  }

  // Check single permission
  if (options.permission && !userPermissions.includes(options.permission)) {
    throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
  }

  // Check any permissions (OR logic)
  if (options.anyPermissions) {
    const hasAny = options.anyPermissions.some(p => userPermissions.includes(p))
    if (!hasAny) {
      throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
    }
  }

  // Check all permissions (AND logic)
  if (options.allPermissions) {
    const hasAll = options.allPermissions.every(p => userPermissions.includes(p))
    if (!hasAll) {
      throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
    }
  }
}

/**
 * Route guard that requires admin access
 * Throws a redirect to /errors/forbidden if user is not admin
 */
export function requireAdmin() {
  requirePermission({ requireAdmin: true })
}
