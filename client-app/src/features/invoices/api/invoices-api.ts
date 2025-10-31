import api from "@/lib/api";
import type { BackendPageResponse } from '@/lib/api-utils';
import type { Invoice, InvoiceFormData } from './schema';

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/invoices";

export const invoicesApi = {
  create: async (data: InvoiceFormData): Promise<ApiResponse<Invoice>> => {
    const response = await api.post<ApiResponse<Invoice>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "invoiceDate",
    sortDirection: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<Invoice>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Invoice>>>(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  search: async (
    searchTerm: string,
    page = 0,
    size = 10,
    sortBy = "invoiceDate",
    sortDirection: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<Invoice>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Invoice>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<Invoice[]>> => {
    const response = await api.get<ApiResponse<Invoice[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getByPayeeId: async (payeeId: number): Promise<ApiResponse<Invoice[]>> => {
    const response = await api.get<ApiResponse<Invoice[]>>(`${BASE_URL}/payee/${payeeId}`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<Invoice>> => {
    const response = await api.get<ApiResponse<Invoice>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: InvoiceFormData): Promise<ApiResponse<Invoice>> => {
    const response = await api.put<ApiResponse<Invoice>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  updatePaymentStatus: async (id: number, paymentStatus: string): Promise<ApiResponse<Invoice>> => {
    const response = await api.put<ApiResponse<Invoice>>(
      `${BASE_URL}/${id}/payment-status`,
      { paymentStatus }
    );
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },
};
