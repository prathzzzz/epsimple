import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import { BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { AssetsOnDatacenter, AssetsOnDatacenterFormData } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const ASSETS_ON_Datacenter_ENDPOINTS = {
  BASE: '/api/assets-on-datacenter',
  SEARCH: '/api/assets-on-datacenter/search',
  BY_Datacenter: (DatacenterId: number) => `/api/assets-on-datacenter/datacenter/${DatacenterId}`,
  BY_ID: (id: number) => `/api/assets-on-datacenter/${id}`,
};

export const assetsOnDatacenterApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['assets-on-Datacenter', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetsOnDatacenter>>>(
          ASSETS_ON_Datacenter_ENDPOINTS.BASE,
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
      queryKey: ['assets-on-Datacenter', 'search', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetsOnDatacenter>>>(
          ASSETS_ON_Datacenter_ENDPOINTS.SEARCH,
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
      mutationFn: async (data: AssetsOnDatacenterFormData) => {
        const response = await api.post<ApiResponse<AssetsOnDatacenter>>(
          ASSETS_ON_Datacenter_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-Datacenter'] });
        toast.success('Asset placed in Datacenter successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to place asset in Datacenter');
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: AssetsOnDatacenterFormData }) => {
        const response = await api.put<ApiResponse<AssetsOnDatacenter>>(
          ASSETS_ON_Datacenter_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-Datacenter'] });
        toast.success('Asset in Datacenter updated successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to update asset in Datacenter');
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(ASSETS_ON_Datacenter_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-Datacenter'] });
        toast.success('Asset removed from Datacenter successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to remove asset from Datacenter');
      },
    });
  },
};
