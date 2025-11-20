import { useAuth } from '@/context/auth-provider'

/**
 * Hook for checking user permissions
 * Provides convenient methods to check if user has specific permissions
 */
export function usePermission() {
  const { user } = useAuth()

  /**
   * Check if user has a specific permission
   * @param permission - Permission string like "ASSET:CREATE"
   * @returns true if user has the permission or is admin
   */
  const hasPermission = (permission: string): boolean => {
    if (!user) return false
    
    // Check if user has "ALL" permission (admin) in allPermissions
    if (user.allPermissions?.includes('ALL')) return true
    
    // Fallback: Check roles if allPermissions is null/undefined
    if (!user.allPermissions || user.allPermissions.length === 0) {
      // Check if any role has the permission
      return user.roles?.some(role => 
        role.permissions?.some(p => p.name === 'ALL' || p.name === permission)
      ) ?? false
    }
    
    // Check if user has the specific permission
    return user.allPermissions?.includes(permission) ?? false
  }

  /**
   * Check if user has ANY of the specified permissions
   * @param permissions - Array of permission strings
   * @returns true if user has at least one of the permissions
   */
  const hasAnyPermission = (permissions: string[]): boolean => {
    if (!user) return false
    
    // Admin has all permissions
    if (user.allPermissions?.includes('ALL')) return true
    
    // Fallback: Check roles if allPermissions is null/undefined
    if (!user.allPermissions || user.allPermissions.length === 0) {
      return user.roles?.some(role => 
        role.permissions?.some(p => 
          p.name === 'ALL' || permissions.includes(p.name)
        )
      ) ?? false
    }
    
    // Check if user has any of the permissions
    return permissions.some(permission => 
      user.allPermissions?.includes(permission)
    )
  }

  /**
   * Check if user has ALL of the specified permissions
   * @param permissions - Array of permission strings
   * @returns true if user has all of the permissions
   */
  const hasAllPermissions = (permissions: string[]): boolean => {
    if (!user) return false
    
    // Admin has all permissions
    if (user.allPermissions?.includes('ALL')) return true
    
    // Fallback: Check roles if allPermissions is null/undefined
    if (!user.allPermissions || user.allPermissions.length === 0) {
      // If user has ALL permission in any role, return true
      const hasAllPerm = user.roles?.some(role => 
        role.permissions?.some(p => p.name === 'ALL')
      )
      if (hasAllPerm) return true
      
      // Otherwise check if user has all specific permissions
      return permissions.every(permission => 
        user.roles?.some(role => 
          role.permissions?.some(p => p.name === permission)
        ) ?? false
      )
    }
    
    // Check if user has all of the permissions
    return permissions.every(permission => 
      user.allPermissions?.includes(permission)
    )
  }

  /**
   * Check if user is admin
   * @returns true if user has "ALL" permission
   */
  const isAdmin = (): boolean => {
    if (!user) return false
    
    // Check allPermissions first
    if (user.allPermissions?.includes('ALL')) return true
    
    // Fallback: Check if any role has ALL permission
    return user.roles?.some(role => 
      role.permissions?.some(p => p.name === 'ALL')
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
