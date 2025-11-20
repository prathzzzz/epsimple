import React from 'react'
import { PermissionGuard } from './permission-guard'

interface AdminGuardProps {
  children: React.ReactNode
  /** Fallback component to show when user is not admin */
  fallback?: React.ReactNode
}

/**
 * Guard component that only renders children for admin users
 * Wrapper around PermissionGuard with requireAdmin=true
 * 
 * @example
 * <AdminGuard>
 *   <AdminPanel />
 * </AdminGuard>
 * 
 * @example
 * <AdminGuard fallback={<AccessDenied />}>
 *   <CoreMasterPage />
 * </AdminGuard>
 */
export function AdminGuard({ children, fallback }: AdminGuardProps) {
  return (
    <PermissionGuard requireAdmin fallback={fallback}>
      {children}
    </PermissionGuard>
  )
}
