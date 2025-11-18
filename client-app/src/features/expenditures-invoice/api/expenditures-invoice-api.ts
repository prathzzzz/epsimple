import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from "@/lib/api";
import type { BackendPageResponse } from '@/lib/api-utils';
import type { ExpendituresInvoice, ExpendituresInvoiceFormData } from "./schema";

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/expenditures/invoices";

export const expendituresInvoiceApi = {
  create: async (data: ExpendituresInvoiceFormData): Promise<ApiResponse<ExpendituresInvoice>> => {
    const response = await api.post<ApiResponse<ExpendituresInvoice>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortOrder: "ASC" | "DESC" = "DESC"
  ): Promise<ApiResponse<BackendPageResponse<ExpendituresInvoice>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<ExpendituresInvoice>>>(BASE_URL, {
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
  ): Promise<ApiResponse<BackendPageResponse<ExpendituresInvoice>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<ExpendituresInvoice>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortOrder },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<ExpendituresInvoice[]>> => {
    const response = await api.get<ApiResponse<ExpendituresInvoice[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<ExpendituresInvoice>> => {
    const response = await api.get<ApiResponse<ExpendituresInvoice>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: ExpendituresInvoiceFormData): Promise<ApiResponse<ExpendituresInvoice>> => {
    const response = await api.put<ApiResponse<ExpendituresInvoice>>(`${BASE_URL}/${id}`, data);
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
  ): Promise<ApiResponse<BackendPageResponse<ExpendituresInvoice>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<ExpendituresInvoice>>>(
      `${BASE_URL}/project/${projectId}`,
      {
        params: { page, size, sortBy, sortOrder },
      }
    );
    return response.data;
  },

  getByInvoice: async (invoiceId: number): Promise<ApiResponse<ExpendituresInvoice[]>> => {
    const response = await api.get<ApiResponse<ExpendituresInvoice[]>>(
      `${BASE_URL}/invoice/${invoiceId}`
    );
    return response.data;
  },

  // React Query hooks
  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: ExpendituresInvoiceFormData) => {
        const response = await expendituresInvoiceApi.create(data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['expenditures-invoices'] });
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
      mutationFn: async ({ id, data }: { id: number; data: ExpendituresInvoiceFormData }) => {
        const response = await expendituresInvoiceApi.update(id, data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['expenditures-invoices'] });
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
        await expendituresInvoiceApi.delete(id);
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['expenditures-invoices'] });
        toast.success('Expenditure deleted successfully');
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || 'Failed to delete expenditure';
        toast.error(message);
      },
    });
  },
};
