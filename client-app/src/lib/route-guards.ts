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
 * Normalize permission string to uppercase for consistent comparison
 */
const normalizePermission = (permission: string): string => permission.toUpperCase()

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

  // Normalize all user permissions to uppercase for case-insensitive comparison
  const userPermissions = (user.allPermissions || []).map(normalizePermission)
  const isUserAdmin = userPermissions.includes('ALL')

  // Admin has access to everything
  if (isUserAdmin) {
    return
  }

  // Check admin requirement
  if (options.requireAdmin) {
    throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
  }

  // Check single permission (case-insensitive)
  if (options.permission && !userPermissions.includes(normalizePermission(options.permission))) {
    throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
  }

  // Check any permissions (OR logic, case-insensitive)
  if (options.anyPermissions) {
    const normalizedRequired = options.anyPermissions.map(normalizePermission)
    const hasAny = normalizedRequired.some(p => userPermissions.includes(p))
    if (!hasAny) {
      throw redirect({ to: '/errors/$error', params: { error: 'forbidden' } })
    }
  }

  // Check all permissions (AND logic, case-insensitive)
  if (options.allPermissions) {
    const normalizedRequired = options.allPermissions.map(normalizePermission)
    const hasAll = normalizedRequired.every(p => userPermissions.includes(p))
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
