import api from '@/lib/api'
import { flattenPageResponse, type BackendPageResponse, type FlatPageResponse } from '@/lib/api-utils'
import { useQuery } from '@tanstack/react-query'

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

  // Bulk upload with SSE
  bulkUpload: async (file: File, onProgress: (progress: BulkUploadProgress) => void): Promise<void> => {
    const formData = new FormData()
    formData.append('file', file)

    // Get auth token from cookie or storage
    const getAuthToken = (): string | null => {
      // Try to get JWT from cookie
      const cookies = document.cookie.split(';')
      const jwtCookie = cookies.find(c => c.trim().startsWith('jwt-token='))
      if (jwtCookie) {
        return jwtCookie.split('=')[1]
      }
      return null
    }

    const token = getAuthToken()
    const headers: HeadersInit = {
      // Don't set Content-Type for FormData, browser will set it with boundary
    }

    if (token) {
      headers['Authorization'] = `Bearer ${token}`
    }

    const response = await fetch(`${import.meta.env.VITE_API_URL}/api/states/bulk-upload`, {
      method: 'POST',
      body: formData,
      credentials: 'include',
      headers,
    })

    if (!response.ok) {
      if (response.status === 401) {
        throw new Error('Authentication required. Please log in again.')
      }
      throw new Error(`Upload failed: ${response.statusText}`)
    }

    const reader = response.body?.getReader()
    const decoder = new TextDecoder()

    if (!reader) {
      throw new Error('Response body is not readable')
    }

    try {
      while (true) {
        const { done, value } = await reader.read()
        if (done) break

        const chunk = decoder.decode(value)
        const lines = chunk.split('\n')

        for (const line of lines) {
          if (line.startsWith('data:')) {
            const data = line.substring(5).trim()
            if (data) {
              try {
                const progress: BulkUploadProgress = JSON.parse(data)
                onProgress(progress)
              } catch (_error) {
                // Ignore parsing errors for SSE data
              }
            }
          }
        }
      }
    } finally {
      reader.releaseLock()
    }
  },

  // Export states to Excel
  exportToExcel: async (): Promise<Blob> => {
    const response = await api.get('/api/states/export', {
      responseType: 'blob',
    })
    return response.data
  },

  // Download template
  downloadTemplate: async (): Promise<Blob> => {
    const response = await api.get('/api/states/download-template', {
      responseType: 'blob',
    })
    return response.data
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

export interface BulkUploadProgress {
  status: 'PROCESSING' | 'COMPLETED' | 'COMPLETED_WITH_ERRORS' | 'FAILED'
  totalRecords: number
  processedRecords: number
  successCount: number
  failureCount: number
  progressPercentage: number
  message: string
  errors?: BulkUploadError[]
  timestamp: string
}

export interface BulkUploadError {
  rowNumber: number
  fieldName: string
  errorMessage: string
  rejectedValue?: string
}
