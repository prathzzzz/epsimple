import api from '@/lib/api'

export interface RoleDTO {
  id: number
  name: string
  description: string
  isActive: boolean
  isSystemRole: boolean
  permissions: PermissionDTO[]
}

export interface PermissionDTO {
  id: number
  name: string
  description: string
}

export interface UserDTO {
  id: number
  email: string
  name: string
  isActive: boolean
  roles: RoleDTO[]
  allPermissions: string[]
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
  timestamp: string
}

export interface CreateUserRequest {
  email: string
  name: string
  password?: string
  roleIds?: number[]
}

export interface UpdateUserRequest {
  email?: string
  name?: string
  isActive?: boolean
}

export const usersApi = {
  // Get all users (to be implemented in backend)
  getAll: async (): Promise<ApiResponse<UserDTO[]>> => {
    const { data } = await api.get<ApiResponse<UserDTO[]>>('/api/users')
    return data
  },

  // Get user by ID (to be implemented in backend)
  getById: async (id: number): Promise<ApiResponse<UserDTO>> => {
    const { data } = await api.get<ApiResponse<UserDTO>>(`/api/users/${id}`)
    return data
  },

  // Create user (to be implemented in backend)
  create: async (request: CreateUserRequest): Promise<ApiResponse<UserDTO>> => {
    const { data } = await api.post<ApiResponse<UserDTO>>('/api/users', request)
    return data
  },

  // Update user (to be implemented in backend)
  update: async (id: number, request: UpdateUserRequest): Promise<ApiResponse<UserDTO>> => {
    const { data } = await api.put<ApiResponse<UserDTO>>(`/api/users/${id}`, request)
    return data
  },

  // Delete user (to be implemented in backend)
  delete: async (id: number): Promise<ApiResponse<void>> => {
    const { data } = await api.delete<ApiResponse<void>>(`/api/users/${id}`)
    return data
  },

  // Get current user (existing endpoint)
  getCurrentUser: async (): Promise<ApiResponse<UserDTO>> => {
    const { data } = await api.get<ApiResponse<UserDTO>>('/api/auth/me')
    return data
  },
}
