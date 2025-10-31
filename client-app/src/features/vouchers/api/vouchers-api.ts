import api from "@/lib/api";
import type { BackendPageResponse } from "@/lib/api-utils";
import type { Voucher, VoucherFormData } from "./schema";

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/vouchers";

export const vouchersApi = {
  create: async (data: VoucherFormData): Promise<ApiResponse<Voucher>> => {
    const response = await api.post<ApiResponse<Voucher>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "voucherDate",
    sortDirection: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<Voucher>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Voucher>>>(
      BASE_URL,
      {
        params: { page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  search: async (
    searchTerm: string,
    page = 0,
    size = 10,
    sortBy = "voucherDate",
    sortDirection: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<Voucher>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Voucher>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<Voucher[]>> => {
    const response = await api.get<ApiResponse<Voucher[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getByPayee: async (
    payeeId: number,
    page = 0,
    size = 10,
    sortBy = "voucherDate",
    sortDirection: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<Voucher>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Voucher>>>(
      `${BASE_URL}/payee/${payeeId}`,
      {
        params: { page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<Voucher>> => {
    const response = await api.get<ApiResponse<Voucher>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (
    id: number,
    data: VoucherFormData
  ): Promise<ApiResponse<Voucher>> => {
    const response = await api.put<ApiResponse<Voucher>>(
      `${BASE_URL}/${id}`,
      data
    );
    return response.data;
  },

  updatePaymentStatus: async (
    id: number,
    paymentStatus: string
  ): Promise<ApiResponse<Voucher>> => {
    const response = await api.put<ApiResponse<Voucher>>(
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
