import { useAuth } from '@/context/auth-provider'

/**
 * Normalize permission string to uppercase for consistent comparison
 */
const normalizePermission = (permission: string): string => permission.toUpperCase()

/**
 * Hook for checking user permissions
 * Provides convenient methods to check if user has specific permissions
 */
export function usePermission() {
  const { user } = useAuth()

  // Get normalized user permissions (uppercase)
  const getNormalizedUserPermissions = (): string[] => {
    if (user?.allPermissions && user.allPermissions.length > 0) {
      return user.allPermissions.map(normalizePermission)
    }
    
    // Fallback: Extract permissions from roles
    if (user?.roles) {
      const permissions = new Set<string>()
      user.roles.forEach(role => {
        role.permissions?.forEach(p => {
          permissions.add(normalizePermission(p.name))
        })
      })
      return Array.from(permissions)
    }
    
    return []
  }

  /**
   * Check if user has a specific permission
   * @param permission - Permission string like "ASSET:CREATE"
   * @returns true if user has the permission or is admin
   */
  const hasPermission = (permission: string): boolean => {
    if (!user) return false
    
    const normalizedPermission = normalizePermission(permission)
    const userPermissions = getNormalizedUserPermissions()
    
    // Check if user has "ALL" permission (admin)
    if (userPermissions.includes('ALL')) return true
    
    // Fallback: Check roles if allPermissions is null/undefined
    if (!user.allPermissions || user.allPermissions.length === 0) {
      // Check if any role has the permission
      return user.roles?.some(role => 
        role.permissions?.some(p => 
          normalizePermission(p.name) === 'ALL' || normalizePermission(p.name) === normalizedPermission
        )
      ) ?? false
    }
    
    // Check if user has the specific permission
    return userPermissions.includes(normalizedPermission)
  }

  /**
   * Check if user has ANY of the specified permissions
   * @param permissions - Array of permission strings
   * @returns true if user has at least one of the permissions
   */
  const hasAnyPermission = (permissions: string[]): boolean => {
    if (!user) return false
    
    const userPermissions = getNormalizedUserPermissions()
    const normalizedPermissions = permissions.map(normalizePermission)
    
    // Admin has all permissions
    if (userPermissions.includes('ALL')) return true
    
    // Fallback: Check roles if allPermissions is null/undefined
    if (!user.allPermissions || user.allPermissions.length === 0) {
      return user.roles?.some(role => 
        role.permissions?.some(p => 
          normalizePermission(p.name) === 'ALL' || normalizedPermissions.includes(normalizePermission(p.name))
        )
      ) ?? false
    }
    
    // Check if user has any of the permissions
    return normalizedPermissions.some(permission => 
      userPermissions.includes(permission)
    )
  }

  /**
   * Check if user has ALL of the specified permissions
   * @param permissions - Array of permission strings
   * @returns true if user has all of the permissions
   */
  const hasAllPermissions = (permissions: string[]): boolean => {
    if (!user) return false
    
    const userPermissions = getNormalizedUserPermissions()
    const normalizedPermissions = permissions.map(normalizePermission)
    
    // Admin has all permissions
    if (userPermissions.includes('ALL')) return true
    
    // Fallback: Check roles if allPermissions is null/undefined
    if (!user.allPermissions || user.allPermissions.length === 0) {
      // If user has ALL permission in any role, return true
      const hasAllPerm = user.roles?.some(role => 
        role.permissions?.some(p => normalizePermission(p.name) === 'ALL')
      )
      if (hasAllPerm) return true
      
      // Otherwise check if user has all specific permissions
      return normalizedPermissions.every(permission => 
        user.roles?.some(role => 
          role.permissions?.some(p => normalizePermission(p.name) === permission)
        ) ?? false
      )
    }
    
    // Check if user has all of the permissions
    return normalizedPermissions.every(permission => 
      userPermissions.includes(permission)
    )
  }

  /**
   * Check if user is admin
   * @returns true if user has "ALL" permission
   */
  const isAdmin = (): boolean => {
    if (!user) return false
    
    const userPermissions = getNormalizedUserPermissions()
    
    // Check allPermissions first
    if (userPermissions.includes('ALL')) return true
    
    // Fallback: Check if any role has ALL permission
    return user.roles?.some(role => 
      role.permissions?.some(p => normalizePermission(p.name) === 'ALL')
    ) ?? false
  }

  /**
   * Check if user can perform an action on a scope
   * @param scope - Scope like "ASSET", "SITE"
   * @param action - Action like "CREATE", "READ", "UPDATE", "DELETE"
   * @returns true if user has the permission
   */
  const can = (scope: string, action: string): boolean => {
    return hasPermission(`${scope}:${action}`)
  }

  /**
   * Get all user permissions
   * @returns Array of permission strings
   */
  const getPermissions = (): string[] => {
    if (user?.allPermissions && user.allPermissions.length > 0) {
      return user.allPermissions
    }
    
    // Fallback: Extract permissions from roles
    if (user?.roles) {
      const permissions = new Set<string>()
      user.roles.forEach(role => {
        role.permissions?.forEach(p => {
          permissions.add(p.name)
        })
      })
      return Array.from(permissions).sort()
    }
    
    return []
  }

  /**
   * Check if user has a specific role
   * @param roleName - Role name like "ADMIN"
   * @returns true if user has the role
   */
  const hasRole = (roleName: string): boolean => {
    if (!user) return false
    return user.roles?.some(role => role.name === roleName) ?? false
  }

  return {
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    isAdmin,
    can,
    getPermissions,
    hasRole,
  }
}
