import api from '@/lib/api';
import { useQuery } from '@tanstack/react-query';
import { type BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { AssetTypeFormData } from '@/features/asset-types/api/schema';

const BASE_URL = '/api/asset-types';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

export interface AssetType {
  id: number;
  typeName: string;
  typeDescription: string | null;
  assetCategoryId: number;
  assetCategoryName: string;
  createdAt: string;
  updatedAt: string;
}

export const assetTypesApi = {
  // Create asset type
  create: async (data: AssetTypeFormData) => {
    const response = await api.post(BASE_URL, data);
    return response.data;
  },

  // Get all asset types (paginated)
  getAll: async (page: number, size: number, sortBy: string, sortDirection: string) => {
    const response = await api.get(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  // Search asset types
  search: async (searchTerm: string, page: number, size: number, sortBy: string, sortDirection: string) => {
    const response = await api.get(`${BASE_URL}/search`, {
      params: { searchTerm, page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  // Get asset types list (no pagination)
  getList: async () => {
    const response = await api.get(`${BASE_URL}/list`);
    return response.data.data; // Unwrap the ApiResponse wrapper
  },

  // Get asset type by ID
  getById: async (id: number) => {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  // Update asset type
  update: async (id: number, data: AssetTypeFormData) => {
    const response = await api.put(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  // Delete asset type
  delete: async (id: number) => {
    const response = await api.delete(`${BASE_URL}/${id}`);
    return response.data;
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['asset-types', 'search', searchTerm],
      queryFn: async () => {
        const endpoint = searchTerm?.trim() ? `${BASE_URL}/search` : BASE_URL;
        const params: Record<string, unknown> = {
          page: 0,
          size: 20,
          sortBy: 'typeName',
          sortDirection: 'ASC',
        };
        
        if (searchTerm?.trim()) {
          params.searchTerm = searchTerm.trim();
        }
        
        const response = await api.get<ApiResponse<BackendPageResponse<AssetType>>>(endpoint, {
          params,
        });
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },
};
