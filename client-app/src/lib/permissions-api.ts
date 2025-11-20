import api from '@/lib/api'

// ==================== TYPES ====================

export interface PermissionDTO {
  id: number
  name: string
  description: string
  scope: string
  action: string
  category: string
  isSystemPermission: boolean
  isActive: boolean
}

export interface PermissionsByCategory {
  [category: string]: PermissionDTO[]
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
  timestamp: string
}

// ==================== PERMISSIONS API ====================

export const permissionsApi = {
  /**
   * Get all permissions
   */
  getAllPermissions: async (): Promise<PermissionDTO[]> => {
    const response = await api.get<ApiResponse<PermissionDTO[]>>('/api/permissions')
    return response.data.data
  },

  /**
   * Get permissions grouped by category
   */
  getPermissionsByCategory: async (): Promise<PermissionsByCategory> => {
    const response = await api.get<ApiResponse<PermissionsByCategory>>(
      '/api/permissions/grouped'
    )
    return response.data.data
  },

  /**
   * Get permissions for a specific scope
   */
  getPermissionsByScope: async (scope: string): Promise<PermissionDTO[]> => {
    const response = await api.get<ApiResponse<PermissionDTO[]>>(
      `/api/permissions/scope/${scope}`
    )
    return response.data.data
  },

  /**
   * Get current user's permissions
   */
  getMyPermissions: async (): Promise<string[]> => {
    const response = await api.get<ApiResponse<string[]>>('/api/permissions/my')
    return response.data.data
  },
}
