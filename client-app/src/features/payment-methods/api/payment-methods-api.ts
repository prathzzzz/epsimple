import api from "@/lib/api";
import { useQuery } from "@tanstack/react-query";
import { type BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';

export interface PaymentMethod {
  id: number;
  methodName: string;
  description: string | null;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface PaymentMethodFormData {
  methodName: string;
  description?: string;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/payment-methods";

export const paymentMethodsApi = {
  create: async (data: PaymentMethodFormData): Promise<ApiResponse<PaymentMethod>> => {
    const response = await api.post<ApiResponse<PaymentMethod>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<BackendPageResponse<PaymentMethod>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<PaymentMethod>>>(BASE_URL, {
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
  ): Promise<ApiResponse<BackendPageResponse<PaymentMethod>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<PaymentMethod>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<PaymentMethod[]>> => {
    const response = await api.get<ApiResponse<PaymentMethod[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<PaymentMethod>> => {
    const response = await api.get<ApiResponse<PaymentMethod>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: PaymentMethodFormData): Promise<ApiResponse<PaymentMethod>> => {
    const response = await api.put<ApiResponse<PaymentMethod>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['payment-methods', 'search', searchTerm],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<PaymentMethod>>>(
          searchTerm?.trim() ? `${BASE_URL}/search` : BASE_URL,
          {
            params: {
              ...(searchTerm?.trim() && { searchTerm: searchTerm.trim() }),
              page: 0,
              size: 20,
              sortBy: 'methodName',
              sortDirection: 'ASC',
            },
          }
        );
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },
};
