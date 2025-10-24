import api from './api'
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from './api-utils'

export interface PersonType {
  id: number
  typeName: string
  description?: string
  createdAt: string
  updatedAt: string
}

export interface PersonTypeFormData {
  typeName: string
  description?: string
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
}

const BASE_URL = '/epsone/api/person-types'

export const personTypesApi = {
  // Get all person types with pagination
  getAll: async (params: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'ASC' | 'DESC'
  }): Promise<ApiResponse<FlatPageResponse<PersonType>>> => {
    const searchParams = new URLSearchParams({
      page: params.page?.toString() || '0',
      size: params.size?.toString() || '10',
      sortBy: params.sortBy || 'id',
      sortDirection: params.sortDirection || 'ASC',
    })

    const response = await fetch(`${BASE_URL}?${searchParams}`)
    if (!response.ok) {
      throw new Error('Failed to fetch person types')
    }
    const json: ApiResponse<BackendPageResponse<PersonType>> = await response.json()
    return {
      ...json,
      data: flattenPageResponse(json.data)
    }
  },

  // Search person types
  search: async (
    query: string,
    params: {
      page?: number
      size?: number
      sortBy?: string
      sortDirection?: 'ASC' | 'DESC'
    }
  ): Promise<ApiResponse<FlatPageResponse<PersonType>>> => {
    const searchParams = new URLSearchParams({
      query,
      page: params.page?.toString() || '0',
      size: params.size?.toString() || '10',
      sortBy: params.sortBy || 'id',
      sortDirection: params.sortDirection || 'ASC',
    })

    const response = await fetch(`${BASE_URL}/search?${searchParams}`)
    if (!response.ok) {
      throw new Error('Failed to search person types')
    }
    const json: ApiResponse<BackendPageResponse<PersonType>> = await response.json()
    return {
      ...json,
      data: flattenPageResponse(json.data)
    }
  },

  // Get all person types as list (no pagination)
  getList: async (): Promise<ApiResponse<PersonType[]>> => {
    const response = await fetch(`${BASE_URL}/list`)
    if (!response.ok) {
      throw new Error('Failed to fetch person types list')
    }
    return response.json()
  },

  // Get person type by ID
  getById: async (id: number): Promise<ApiResponse<PersonType>> => {
    const response = await fetch(`${BASE_URL}/${id}`)
    if (!response.ok) {
      throw new Error('Failed to fetch person type')
    }
    return response.json()
  },

  // Create person type
  create: async (data: PersonTypeFormData): Promise<ApiResponse<PersonType>> => {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || 'Failed to create person type')
    }
    return response.json()
  },

  // Update person type
  update: async (
    id: number,
    data: PersonTypeFormData
  ): Promise<ApiResponse<PersonType>> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    })
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || 'Failed to update person type')
    }
    return response.json()
  },

  // Delete person type
  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'DELETE',
    })
    if (!response.ok) {
      const error = await response.json()
      throw new Error(error.message || 'Failed to delete person type')
    }
    return response.json()
  },
}
