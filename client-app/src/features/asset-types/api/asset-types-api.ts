import api from '@/lib/api';
import type { AssetTypeFormData } from '@/features/asset-types/api/schema';

const BASE_URL = '/api/asset-types';

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
};
