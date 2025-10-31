import api from "@/lib/api";
import type { BackendPageResponse } from '@/lib/api-utils';
import type { PaymentDetails, PaymentDetailsFormData } from './schema';

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/payment-details";

export const paymentDetailsApi = {
  create: async (data: PaymentDetailsFormData): Promise<ApiResponse<PaymentDetails>> => {
    const response = await api.post<ApiResponse<PaymentDetails>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "paymentDate",
    sortDirection: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<PaymentDetails>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<PaymentDetails>>>(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  search: async (
    searchTerm: string,
    page = 0,
    size = 10,
    sortBy = "paymentDate",
    sortDirection: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<PaymentDetails>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<PaymentDetails>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<PaymentDetails[]>> => {
    const response = await api.get<ApiResponse<PaymentDetails[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<PaymentDetails>> => {
    const response = await api.get<ApiResponse<PaymentDetails>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: PaymentDetailsFormData): Promise<ApiResponse<PaymentDetails>> => {
    const response = await api.put<ApiResponse<PaymentDetails>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },
};
