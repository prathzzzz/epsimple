import React from 'react'
import { usePermission } from '@/hooks/use-permission'

interface PermissionGuardProps {
  children: React.ReactNode
  /** Single permission required */
  permission?: string
  /** Any of these permissions (OR logic) */
  anyPermissions?: string[]
  /** All of these permissions (AND logic) */
  allPermissions?: string[]
  /** Require admin access */
  requireAdmin?: boolean
  /** Fallback component to show when permission is denied */
  fallback?: React.ReactNode
}

/**
 * Guard component that conditionally renders children based on user permissions
 * 
 * @example
 * // Single permission
 * <PermissionGuard permission="ASSET:CREATE">
 *   <CreateButton />
 * </PermissionGuard>
 * 
 * @example
 * // Any permission (OR)
 * <PermissionGuard anyPermissions={["ASSET:CREATE", "ASSET:UPDATE"]}>
 *   <ActionButton />
 * </PermissionGuard>
 * 
 * @example
 * // All permissions (AND)
 * <PermissionGuard allPermissions={["ASSET:READ", "SITE:READ"]}>
 *   <ReportButton />
 * </PermissionGuard>
 * 
 * @example
 * // With fallback
 * <PermissionGuard permission="ASSET:DELETE" fallback={<div>No access</div>}>
 *   <DeleteButton />
 * </PermissionGuard>
 */
export function PermissionGuard({
  children,
  permission,
  anyPermissions,
  allPermissions,
  requireAdmin,
  fallback = null,
}: PermissionGuardProps) {
  const {
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    isAdmin,
  } = usePermission()

  // Check admin requirement first
  if (requireAdmin && !isAdmin()) {
    return <>{fallback}</>
  }

  // Check single permission
  if (permission && !hasPermission(permission)) {
    return <>{fallback}</>
  }

  // Check any permissions (OR logic)
  if (anyPermissions && !hasAnyPermission(anyPermissions)) {
    return <>{fallback}</>
  }

  // Check all permissions (AND logic)
  if (allPermissions && !hasAllPermissions(allPermissions)) {
    return <>{fallback}</>
  }

  return <>{children}</>
}
