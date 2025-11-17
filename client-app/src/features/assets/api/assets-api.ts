import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import api from '@/lib/api'
import { type BackendPageResponse, flattenPageResponse } from '@/lib/api-utils'
import type { Asset, AssetRequest } from './schema'

const ASSET_ENDPOINTS = {
  BASE: '/api/assets',
  SEARCH: '/api/assets/search',
  LIST: '/api/assets/list',
  BY_ID: (id: number) => `/api/assets/${id}`,
}

interface ApiResponse<T> {
  data: T
  message: string
  status: number
  timestamp: string
}

export const assetsApi = {
  useGetAll: (params: {
    page: number
    size: number
    sortBy?: string
    sortOrder?: string
    search?: string
  }) => {
    return useQuery({
      queryKey: ['assets', params],
      queryFn: async () => {
        const endpoint = params.search ? ASSET_ENDPOINTS.SEARCH : ASSET_ENDPOINTS.BASE
        const response = await api.get<ApiResponse<BackendPageResponse<Asset>>>(
          endpoint,
          {
            params: {
              page: params.page,
              size: params.size,
              ...(params.search && { searchTerm: params.search }),
              ...(params.sortBy && { sortBy: params.sortBy }),
              ...(params.sortOrder && { sortDir: params.sortOrder }),
            },
          }
        )
        return flattenPageResponse(response.data.data)
      },
    })
  },

  useGetById: (id: number | null) => {
    return useQuery({
      queryKey: ['assets', id],
      queryFn: async () => {
        if (!id) throw new Error('ID is required')
        const response = await api.get<ApiResponse<Asset>>(ASSET_ENDPOINTS.BY_ID(id))
        return response.data.data
      },
      enabled: !!id,
    })
  },

  useCreate: () => {
    const queryClient = useQueryClient()

    return useMutation({
      mutationFn: async (data: AssetRequest) => {
        const response = await api.post<ApiResponse<Asset>>(ASSET_ENDPOINTS.BASE, data)
        return response.data.data
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets'] })
        toast.success('Asset created successfully')
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to create asset';
        toast.error(message);
      },
    })
  },

  useUpdate: () => {
    const queryClient = useQueryClient()

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: AssetRequest }) => {
        const response = await api.put<ApiResponse<Asset>>(
          ASSET_ENDPOINTS.BY_ID(id),
          data
        )
        return response.data.data
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets'] })
        toast.success('Asset updated successfully')
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to update asset';
        toast.error(message);
      },
    })
  },

  useDelete: () => {
    const queryClient = useQueryClient()

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(ASSET_ENDPOINTS.BY_ID(id))
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets'] })
        toast.success('Asset deleted successfully')
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to delete asset';
        toast.error(message);
      },
    })
  },

  getList: async (): Promise<Asset[]> => {
    const response = await api.get<ApiResponse<Asset[]>>(ASSET_ENDPOINTS.LIST)
    return response.data.data
  },

  // Search assets with query
  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['assets', 'search', searchTerm],
      queryFn: async () => {
        if (!searchTerm || searchTerm.trim().length === 0) {
          // If no search term, return empty array
          return []
        }
        const response = await api.get<ApiResponse<BackendPageResponse<Asset>>>(
          ASSET_ENDPOINTS.SEARCH,
          {
            params: {
              searchTerm: searchTerm.trim(),
              page: 0,
              size: 50, // Limit to 50 results
            },
          }
        )
        return flattenPageResponse(response.data.data).content
      },
      enabled: searchTerm.trim().length > 0,
      staleTime: 30000, // Cache for 30 seconds
    })
  },
}
