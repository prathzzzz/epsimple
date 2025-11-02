import api from '@/lib/api';
import { BackendPageResponse, flattenPageResponse, FlatPageResponse } from '@/lib/api-utils';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import type { Payee, PayeeFormData } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = '/api/payees';

export const payeeApi = {
  getAll: async (params: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: string;
    search?: string;
  } = {}): Promise<FlatPageResponse<Payee>> => {
    const { page = 0, size = 10, sortBy = 'id', sortDirection = 'ASC', search } = params;

    if (search && search.trim()) {
      const response = await api.get<ApiResponse<BackendPageResponse<Payee>>>(`${BASE_URL}/search`, {
        params: { searchTerm: search, page, size, sortBy, sortDirection },
      });
      return flattenPageResponse(response.data.data);
    }

    const response = await api.get<ApiResponse<BackendPageResponse<Payee>>>(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<ApiResponse<Payee[]>> => {
    const response = await api.get<ApiResponse<Payee[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<Payee>> => {
    const response = await api.get<ApiResponse<Payee>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  create: async (data: PayeeFormData): Promise<ApiResponse<Payee>> => {
    const response = await api.post<ApiResponse<Payee>>(BASE_URL, data);
    return response.data;
  },

  update: async (id: number, data: PayeeFormData): Promise<ApiResponse<Payee>> => {
    const response = await api.put<ApiResponse<Payee>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`${BASE_URL}/${id}`);
  },

  useGetAll: (params: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: string;
    search?: string;
  } = {}) => {
    return useQuery({
      queryKey: ['payees', params],
      queryFn: () => payeeApi.getAll(params),
    });
  },

  useSearch: (params: {
    searchTerm: string;
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['payees', 'search', params],
      queryFn: async () => {
        const endpoint = params.searchTerm?.trim() ? `${BASE_URL}/search` : BASE_URL;
        const queryParams: Record<string, unknown> = {
          page: params.page ?? 0,
          size: params.size ?? 20,
          sortBy: params.sortBy ?? 'id',
          sortDirection: params.sortDirection ?? 'asc',
        };
        
        if (params.searchTerm?.trim()) {
          queryParams.searchTerm = params.searchTerm.trim();
        }
        
        const response = await api.get<ApiResponse<BackendPageResponse<Payee>>>(endpoint, {
          params: queryParams,
        });
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: payeeApi.create,
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['payees'] });
        toast.success('Payee created successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to create payee');
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: ({ id, data }: { id: number; data: PayeeFormData }) =>
        payeeApi.update(id, data),
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['payees'] });
        toast.success('Payee updated successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to update payee');
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: payeeApi.delete,
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['payees'] });
        toast.success('Payee deleted successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to delete payee');
      },
    });
  },
};
