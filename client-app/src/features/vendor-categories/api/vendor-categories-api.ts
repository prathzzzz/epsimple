import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import { type BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';

const BASE_URL = '/api/vendor-categories';

export interface VendorCategory {
  id: number;
  categoryName: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface VendorCategoryFormData {
  categoryName: string;
  description?: string;
}

export interface VendorCategoriesResponse {
  content: VendorCategory[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

export const vendorCategoriesApi = {
  getAll: async (
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    sortDirection: string = 'ASC'
  ): Promise<ApiResponse<VendorCategoriesResponse>> => {
    const response = await axios.get(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  search: async (
    query: string,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    sortDirection: string = 'ASC'
  ): Promise<ApiResponse<VendorCategoriesResponse>> => {
    const response = await axios.get(`${BASE_URL}/search`, {
      params: { query, page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  getList: async (): Promise<ApiResponse<VendorCategory[]>> => {
    const response = await axios.get(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<VendorCategory>> => {
    const response = await axios.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  create: async (data: VendorCategoryFormData): Promise<ApiResponse<VendorCategory>> => {
    const response = await axios.post(BASE_URL, data);
    return response.data;
  },

  update: async (id: number, data: VendorCategoryFormData): Promise<ApiResponse<VendorCategory>> => {
    const response = await axios.put(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await axios.delete(`${BASE_URL}/${id}`);
    return response.data;
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['vendor-categories', 'search', searchTerm],
      queryFn: async () => {
        const endpoint = searchTerm?.trim() ? `${BASE_URL}/search` : BASE_URL;
        const params: Record<string, unknown> = {
          page: 0,
          size: 20,
          sortBy: 'categoryName',
          sortDirection: 'ASC',
        };
        
        if (searchTerm?.trim()) {
          params.query = searchTerm.trim();
        }
        
        const response = await api.get<ApiResponse<BackendPageResponse<VendorCategory>>>(endpoint, {
          params,
        });
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },
};
