import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import { userRolesApi } from '@/lib/user-roles-api'

export const userRoleKeys = {
  all: ['user-roles'] as const,
  permissions: (userId: number) => [...userRoleKeys.all, 'permissions', userId] as const,
}

/**
 * Get user permissions
 */
export function useUserPermissions(userId: number, enabled = true) {
  return useQuery({
    queryKey: userRoleKeys.permissions(userId),
    queryFn: () => userRolesApi.getUserPermissions(userId),
    enabled: enabled && !!userId,
  })
}

/**
 * Assign role to user mutation
 */
export function useAssignRole() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ userId, roleId }: { userId: number; roleId: number }) =>
      userRolesApi.assignRole(userId, roleId),
    onSuccess: () => {
      // Invalidate users queries to refresh the list
      queryClient.invalidateQueries({ queryKey: ['users'] })
    },
    onError: (error: Error) => {
      const apiError = error as { response?: { data?: { message?: string } } }
      toast.error(apiError.response?.data?.message || 'Failed to assign role')
    },
  })
}

/**
 * Remove role from user mutation
 */
export function useRemoveRole() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ userId, roleId }: { userId: number; roleId: number }) =>
      userRolesApi.removeRole(userId, roleId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] })
    },
    onError: (error: Error) => {
      const apiError = error as { response?: { data?: { message?: string } } }
      toast.error(apiError.response?.data?.message || 'Failed to remove role')
    },
  })
}
