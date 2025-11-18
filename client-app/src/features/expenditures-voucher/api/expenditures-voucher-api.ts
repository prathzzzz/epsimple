import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from "@/lib/api";
import type { BackendPageResponse } from '@/lib/api-utils';
import type { ExpendituresVoucher, ExpendituresVoucherFormData } from "./schema";

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/expenditures/vouchers";

export const expendituresVoucherApi = {
  create: async (data: ExpendituresVoucherFormData): Promise<ApiResponse<ExpendituresVoucher>> => {
    const response = await api.post<ApiResponse<ExpendituresVoucher>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortOrder: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<ExpendituresVoucher>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<ExpendituresVoucher>>>(BASE_URL, {
      params: { page, size, sortBy, sortOrder },
    });
    return response.data;
  },

  search: async (
    searchTerm: string,
    page = 0,
    size = 10,
    sortBy = "id",
    sortOrder: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<ExpendituresVoucher>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<ExpendituresVoucher>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortOrder },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<ExpendituresVoucher[]>> => {
    const response = await api.get<ApiResponse<ExpendituresVoucher[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<ExpendituresVoucher>> => {
    const response = await api.get<ApiResponse<ExpendituresVoucher>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: ExpendituresVoucherFormData): Promise<ApiResponse<ExpendituresVoucher>> => {
    const response = await api.put<ApiResponse<ExpendituresVoucher>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`${BASE_URL}/${id}`);
  },

  getByProject: async (
    projectId: number,
    page = 0,
    size = 10,
    sortBy = "id",
    sortOrder: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<ExpendituresVoucher>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<ExpendituresVoucher>>>(
      `${BASE_URL}/project/${projectId}`,
      {
        params: { page, size, sortBy, sortOrder },
      }
    );
    return response.data;
  },

  getByVoucher: async (voucherId: number): Promise<ApiResponse<ExpendituresVoucher[]>> => {
    const response = await api.get<ApiResponse<ExpendituresVoucher[]>>(
      `${BASE_URL}/voucher/${voucherId}`
    );
    return response.data;
  },

  // React Query hooks
  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: ExpendituresVoucherFormData) => {
        const response = await expendituresVoucherApi.create(data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['expenditures-vouchers'] });
        toast.success('Expenditure created successfully');
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || 'Failed to create expenditure';
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: ExpendituresVoucherFormData }) => {
        const response = await expendituresVoucherApi.update(id, data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['expenditures-vouchers'] });
        toast.success('Expenditure updated successfully');
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || 'Failed to update expenditure';
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await expendituresVoucherApi.delete(id);
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['expenditures-vouchers'] });
        toast.success('Expenditure deleted successfully');
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || 'Failed to delete expenditure';
        toast.error(message);
      },
    });
  },
};
