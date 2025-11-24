import api from '@/lib/api'
import { flattenPageResponse, type BackendPageResponse, type FlatPageResponse } from '@/lib/api-utils'
import { useQuery } from '@tanstack/react-query'

/**
 * Helper function to capitalize the first letter of each word in a string
 * Example: "new delhi" -> "New Delhi"
 */
function capitalizeWords(input: string): string {
  if (!input) return input
  
  return input
    .trim()
    .split(/\s+/)
    .map(word => {
      if (!word) return word
      return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
    })
    .join(' ')
}

/**
 * Helper function to transform string to uppercase
 * Example: "mh" -> "MH"
 */
function toUpperCase(input: string): string {
  if (!input) return input
  return input.trim().toUpperCase()
}

/**
 * Transform state form data to ensure proper formatting
 */
function transformStateData(data: StateFormData): StateFormData {
  return {
    stateName: capitalizeWords(data.stateName),
    stateCode: toUpperCase(data.stateCode),
    stateCodeAlt: data.stateCodeAlt ? toUpperCase(data.stateCodeAlt) : data.stateCodeAlt,
  }
}

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
    const transformedData = transformStateData(data)
    const response = await api.post<ApiResponse<State>>('/api/states', transformedData)
    return response.data
  },

  // Update state
  update: async (id: number, data: StateFormData): Promise<ApiResponse<State>> => {
    const transformedData = transformStateData(data)
    const response = await api.put<ApiResponse<State>>(`/api/states/${id}`, transformedData)
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

  // Search hook for dropdowns (client-side filtering)
  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['states', 'search', searchTerm],
      queryFn: async () => {
        const response = await statesApi.getList()
        const allStates = response.data
        
        // If no search term, return first 20 states
        if (!searchTerm || searchTerm.trim().length === 0) {
          return allStates.slice(0, 20)
        }
        
        // Client-side filtering
        return allStates.filter(state => 
          state.stateName.toLowerCase().includes(searchTerm.toLowerCase()) ||
          state.stateCode?.toLowerCase().includes(searchTerm.toLowerCase())
        )
      },
      staleTime: 30000,
    })
  },
}
