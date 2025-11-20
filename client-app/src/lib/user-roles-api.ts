import api from '@/lib/api'

export interface AssignRoleRequest {
  userId: number
  roleId: number
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
  timestamp: string
}

export const userRolesApi = {
  /**
   * Assign role to user (ADMIN only)
   */
  assignRole: async (userId: number, roleId: number): Promise<void> => {
    await api.post<ApiResponse<void>>(`/api/users/${userId}/roles/${roleId}`)
  },

  /**
   * Remove role from user (ADMIN only)
   */
  removeRole: async (userId: number, roleId: number): Promise<void> => {
    await api.delete<ApiResponse<void>>(`/api/users/${userId}/roles/${roleId}`)
  },

  /**
   * Get all permissions for a user
   */
  getUserPermissions: async (userId: number): Promise<string[]> => {
    const response = await api.get<ApiResponse<string[]>>(`/api/users/${userId}/permissions`)
    return response.data.data
  },
}
