import api from '@/lib/api';
import type { MovementTypeFormData } from '@/features/movement-types/api/schema';

const BASE_URL = '/api/movement-types';

export const movementTypesApi = {
  // Create movement type
  create: async (data: MovementTypeFormData) => {
    const response = await api.post(BASE_URL, data);
    return response.data;
  },

  // Get all movement types (paginated)
  getAll: async (page: number, size: number, sortBy: string, sortDirection: string) => {
    const response = await api.get(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  // Search movement types
  search: async (searchTerm: string, page: number, size: number, sortBy: string, sortDirection: string) => {
    const response = await api.get(`${BASE_URL}/search`, {
      params: { searchTerm, page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  // Get movement types list (no pagination)
  getList: async () => {
    const response = await api.get(`${BASE_URL}/list`);
    return response.data;
  },

  // Get movement type by ID
  getById: async (id: number) => {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  // Update movement type
  update: async (id: number, data: MovementTypeFormData) => {
    const response = await api.put(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  // Delete movement type
  delete: async (id: number) => {
    const response = await api.delete(`${BASE_URL}/${id}`);
    return response.data;
  },
};
