import api from '@/lib/api'
import type { Permission } from './auth-api'

// ==================== TYPES ====================

export interface RoleDTO {
  id: number
  name: string
  description: string
  isSystemRole: boolean
  isActive: boolean
  permissions: Permission[]
}

export interface CreateRoleRequest {
  name: string
  description: string
  permissionIds: number[]
}

export interface UpdateRoleRequest {
  name: string
  description: string
}

export interface UpdateRolePermissionsRequest {
  permissionIds: number[]
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
  timestamp: string
}

// ==================== ROLES API ====================

export const rolesApi = {
  /**
   * Get all roles
   */
  getAllRoles: async (): Promise<RoleDTO[]> => {
    const response = await api.get<ApiResponse<RoleDTO[]>>('/api/roles')
    return response.data.data
  },

  /**
   * Get role by ID
   */
  getRoleById: async (id: number): Promise<RoleDTO> => {
    const response = await api.get<ApiResponse<RoleDTO>>(`/api/roles/${id}`)
    return response.data.data
  },

  /**
   * Create new role (ADMIN only)
   */
  createRole: async (data: CreateRoleRequest): Promise<RoleDTO> => {
    const response = await api.post<ApiResponse<RoleDTO>>('/api/roles', data)
    return response.data.data
  },

  /**
   * Update role (ADMIN only)
   */
  updateRole: async (id: number, data: UpdateRoleRequest): Promise<RoleDTO> => {
    const response = await api.put<ApiResponse<RoleDTO>>(`/api/roles/${id}`, data)
    return response.data.data
  },

  /**
   * Delete role (ADMIN only)
   */
  deleteRole: async (id: number): Promise<void> => {
    await api.delete<ApiResponse<void>>(`/api/roles/${id}`)
  },

  /**
   * Update role permissions (ADMIN only)
   */
  updateRolePermissions: async (
    id: number,
    data: UpdateRolePermissionsRequest
  ): Promise<RoleDTO> => {
    const response = await api.put<ApiResponse<RoleDTO>>(
      `/api/roles/${id}/permissions`,
      data
    )
    return response.data.data
  },
}
