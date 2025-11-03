import api from '@/lib/api'
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from '@/lib/api-utils'

export interface State {
  id: number
  stateName: string
  stateCode: string
  stateCodeAlt?: string | null
  createdAt: string
}

export interface StateFormData {
  stateName: string
  stateCode: string
  stateCodeAlt?: string
}

export interface StateListParams {
  page?: number
  size?: number
  search?: string
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
}

export const statesApi = {
  // Get paginated states with optional search
  getAll: async (params: StateListParams = {}): Promise<ApiResponse<FlatPageResponse<State>>> => {
    const { page = 0, size = 10, search = '' } = params
    const queryParams = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      ...(search && { search }),
    })
    
    const response = await api.get<ApiResponse<BackendPageResponse<State>>>(
      `/api/states?${queryParams}`
    )
    return {
      ...response.data,
      data: flattenPageResponse(response.data.data)
    }
  },

  // Get all states as list (for dropdowns)
  getList: async (): Promise<ApiResponse<State[]>> => {
    const response = await api.get<ApiResponse<State[]>>('/api/states/list')
    return response.data
  },

  // Get state by ID
  getById: async (id: number): Promise<ApiResponse<State>> => {
    const response = await api.get<ApiResponse<State>>(`/api/states/${id}`)
    return response.data
  },

  // Create new state
  create: async (data: StateFormData): Promise<ApiResponse<State>> => {
    const response = await api.post<ApiResponse<State>>('/api/states', data)
    return response.data
  },

  // Update state
  update: async (id: number, data: StateFormData): Promise<ApiResponse<State>> => {
    const response = await api.put<ApiResponse<State>>(`/api/states/${id}`, data)
    return response.data
  },

  // Delete state
  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`/api/states/${id}`)
    return response.data
  },

  // Search states
  search: async (query: string, params: { page?: number; size?: number } = {}): Promise<ApiResponse<FlatPageResponse<State>>> => {
    const { page = 0, size = 10 } = params
    const queryParams = new URLSearchParams({
      search: query,
      page: page.toString(),
      size: size.toString(),
    })
    
    const response = await api.get<ApiResponse<BackendPageResponse<State>>>(
      `/api/states/search?${queryParams}`
    )
    return {
      ...response.data,
      data: flattenPageResponse(response.data.data)
    }
  },
}
