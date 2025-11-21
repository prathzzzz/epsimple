import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import {
  rolesApi,
  type CreateRoleRequest,
  type UpdateRoleRequest,
  type UpdateRolePermissionsRequest,
} from '@/lib/roles-api'
import { permissionsApi } from '@/lib/permissions-api'

// ==================== QUERY KEYS ====================

export const roleKeys = {
  all: ['roles'] as const,
  lists: () => [...roleKeys.all, 'list'] as const,
  list: () => [...roleKeys.lists()] as const,
  details: () => [...roleKeys.all, 'detail'] as const,
  detail: (id: number) => [...roleKeys.details(), id] as const,
}

export const permissionKeys = {
  all: ['permissions'] as const,
  lists: () => [...permissionKeys.all, 'list'] as const,
  list: () => [...permissionKeys.lists()] as const,
  grouped: () => [...permissionKeys.all, 'grouped'] as const,
  my: () => [...permissionKeys.all, 'my'] as const,
  scope: (scope: string) => [...permissionKeys.all, 'scope', scope] as const,
}

// ==================== ROLE HOOKS ====================

/**
 * Get all roles
 */
export function useRoles() {
  return useQuery({
    queryKey: roleKeys.list(),
    queryFn: rolesApi.getAllRoles,
  })
}

/**
 * Get role by ID
 */
export function useRole(id: number, enabled = true) {
  return useQuery({
    queryKey: roleKeys.detail(id),
    queryFn: () => rolesApi.getRoleById(id),
    enabled: enabled && !!id,
  })
}

/**
 * Create role mutation
 */
export function useCreateRole() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data: CreateRoleRequest) => rolesApi.createRole(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: roleKeys.lists() })
      toast.success('Role created successfully')
    },
    onError: (error: Error) => {
      const apiError = error as { response?: { data?: { message?: string } } }
      toast.error(apiError.response?.data?.message || 'Failed to create role')
    },
  })
}

/**
 * Update role mutation
 */
export function useUpdateRole() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: UpdateRoleRequest }) =>
      rolesApi.updateRole(id, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: roleKeys.lists() })
      queryClient.invalidateQueries({ queryKey: roleKeys.detail(variables.id) })
      toast.success('Role updated successfully')
    },
    onError: (error: Error) => {
      const apiError = error as { response?: { data?: { message?: string } } }
      toast.error(apiError.response?.data?.message || 'Failed to update role')
    },
  })
}

/**
 * Delete role mutation
 */
export function useDeleteRole() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: number) => rolesApi.deleteRole(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: roleKeys.lists() })
      toast.success('Role deleted successfully')
    },
    onError: (error: Error) => {
      const apiError = error as { response?: { data?: { message?: string } } }
      toast.error(apiError.response?.data?.message || 'Failed to delete role')
    },
  })
}

/**
 * Update role permissions mutation
 */
export function useUpdateRolePermissions() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number
      data: UpdateRolePermissionsRequest
    }) => rolesApi.updateRolePermissions(id, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: roleKeys.lists() })
      queryClient.invalidateQueries({ queryKey: roleKeys.detail(variables.id) })
      toast.success('Role permissions updated successfully')
    },
    onError: (error: Error) => {
      const apiError = error as { response?: { data?: { message?: string } } }
      toast.error(
        apiError.response?.data?.message || 'Failed to update role permissions'
      )
    },
  })
}

// ==================== PERMISSION HOOKS ====================

/**
 * Get all permissions
 */
export function usePermissions() {
  return useQuery({
    queryKey: permissionKeys.list(),
    queryFn: permissionsApi.getAllPermissions,
  })
}

/**
 * Get permissions grouped by category
 */
export function usePermissionsByCategory(enabled = true) {
  return useQuery({
    queryKey: permissionKeys.grouped(),
    queryFn: permissionsApi.getPermissionsByCategory,
    enabled,
  })
}

/**
 * Get permissions for a specific scope
 */
export function usePermissionsByScope(scope: string, enabled = true) {
  return useQuery({
    queryKey: permissionKeys.scope(scope),
    queryFn: () => permissionsApi.getPermissionsByScope(scope),
    enabled: enabled && !!scope,
  })
}

/**
 * Get current user's permissions
 */
export function useMyPermissions() {
  return useQuery({
    queryKey: permissionKeys.my(),
    queryFn: permissionsApi.getMyPermissions,
  })
}
