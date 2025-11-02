import api from "@/lib/api";
import { useQuery } from "@tanstack/react-query";
import { BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';

export interface PayeeType {
  id: number;
  payeeType: string;
  payeeCategory: string | null;
  description: string | null;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface PayeeTypeFormData {
  payeeType: string;
  payeeCategory?: string;
  description?: string;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/payee-types";

export const payeeTypesApi = {
  create: async (data: PayeeTypeFormData): Promise<ApiResponse<PayeeType>> => {
    const response = await api.post<ApiResponse<PayeeType>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<BackendPageResponse<PayeeType>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<PayeeType>>>(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  search: async (
    searchTerm: string,
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<BackendPageResponse<PayeeType>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<PayeeType>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<PayeeType[]>> => {
    const response = await api.get<ApiResponse<PayeeType[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<PayeeType>> => {
    const response = await api.get<ApiResponse<PayeeType>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: PayeeTypeFormData): Promise<ApiResponse<PayeeType>> => {
    const response = await api.put<ApiResponse<PayeeType>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['payee-types', 'search', searchTerm],
      queryFn: async () => {
        const endpoint = searchTerm?.trim() ? `${BASE_URL}/search` : BASE_URL;
        const params: Record<string, unknown> = {
          page: 0,
          size: 20,
          sortBy: 'payeeType',
          sortDirection: 'ASC',
        };
        
        if (searchTerm?.trim()) {
          params.searchTerm = searchTerm.trim();
        }
        
        const response = await api.get<ApiResponse<BackendPageResponse<PayeeType>>>(endpoint, {
          params,
        });
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },
};
