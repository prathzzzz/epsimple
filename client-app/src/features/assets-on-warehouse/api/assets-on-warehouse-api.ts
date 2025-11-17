import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import { type BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { AssetsOnWarehouse, AssetsOnWarehouseFormData } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const ASSETS_ON_WAREHOUSE_ENDPOINTS = {
  BASE: '/api/assets-on-warehouse',
  SEARCH: '/api/assets-on-warehouse/search',
  BY_WAREHOUSE: (warehouseId: number) => `/api/assets-on-warehouse/warehouse/${warehouseId}`,
  BY_ID: (id: number) => `/api/assets-on-warehouse/${id}`,
};

export const assetsOnWarehouseApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['assets-on-warehouse', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetsOnWarehouse>>>(
          ASSETS_ON_WAREHOUSE_ENDPOINTS.BASE,
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
      queryKey: ['assets-on-warehouse', 'search', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetsOnWarehouse>>>(
          ASSETS_ON_WAREHOUSE_ENDPOINTS.SEARCH,
          {
            params: {
              searchTerm: params.searchTerm,
              page: params.page,
              size: params.size,
              sortBy: params.sortBy || 'id',
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
      mutationFn: async (data: AssetsOnWarehouseFormData) => {
        const response = await api.post<ApiResponse<AssetsOnWarehouse>>(
          ASSETS_ON_WAREHOUSE_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-warehouse'] });
        toast.success('Asset placed in warehouse successfully');
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to place asset in warehouse';
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: AssetsOnWarehouseFormData }) => {
        const response = await api.put<ApiResponse<AssetsOnWarehouse>>(
          ASSETS_ON_WAREHOUSE_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-warehouse'] });
        toast.success('Asset in warehouse updated successfully');
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to update asset in warehouse';
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(ASSETS_ON_WAREHOUSE_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-warehouse'] });
        toast.success('Asset removed from warehouse successfully');
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to remove asset from warehouse';
        toast.error(message);
      },
    });
  },
};
