import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import { type BackendPageResponse, type FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { Warehouse, WarehouseFormData } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const WAREHOUSE_ENDPOINTS = {
  BASE: '/api/warehouses',
  SEARCH: '/api/warehouses/search',
  LIST: '/api/warehouses/list',
  BY_ID: (id: number) => `/api/warehouses/${id}`,
};

export const warehouseApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['warehouses', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<Warehouse>>>(
          WAREHOUSE_ENDPOINTS.BASE,
          {
            params: {
              page: params.page,
              size: params.size,
              sortBy: params.sortBy || 'id',
              sortOrder: params.sortDirection || 'asc',
            },
          }
        );
        return flattenPageResponse(response.data.data);
      },
    });
  },

  useSearch: (params: {
    searchTerm: string;
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['warehouses', 'search', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<Warehouse>>>(
          WAREHOUSE_ENDPOINTS.SEARCH,
          {
            params: {
              searchTerm: params.searchTerm,
              page: params.page,
              size: params.size,
              sortBy: params.sortBy || 'warehouseName',
              sortOrder: params.sortDirection || 'asc',
            },
          }
        );
        return flattenPageResponse(response.data.data);
      },
      enabled: !!params.searchTerm,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (data: WarehouseFormData) => {
        const response = await api.post<ApiResponse<Warehouse>>(
          WAREHOUSE_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['warehouses'] });
        toast.success('Warehouse created successfully');
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || "Failed to create warehouse";
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: WarehouseFormData }) => {
        const response = await api.put<ApiResponse<Warehouse>>(
          WAREHOUSE_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['warehouses'] });
        toast.success('Warehouse updated successfully');
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || "Failed to update warehouse";
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(WAREHOUSE_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['warehouses'] });
        toast.success('Warehouse deleted successfully');
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || "Failed to delete warehouse";
        toast.error(message);
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }): Promise<FlatPageResponse<Warehouse>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Warehouse>>>(
      WAREHOUSE_ENDPOINTS.BASE,
      {
        params: {
          page: params.page,
          size: params.size,
          sortBy: params.sortBy || 'id',
          sortOrder: params.sortDirection || 'asc',
        },
      }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<Warehouse[]> => {
    const response = await api.get<ApiResponse<Warehouse[]>>(
      WAREHOUSE_ENDPOINTS.LIST
    );
    return response.data.data;
  },
};
