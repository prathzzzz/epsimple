import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { Datacenter, DatacenterFormValues } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const DATACENTER_ENDPOINTS = {
  BASE: '/api/datacenters',
  SEARCH: '/api/datacenters/search',
  LIST: '/api/datacenters/list',
  BY_ID: (id: number) => `/api/datacenters/${id}`,
};

export const datacenterApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['datacenters', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<Datacenter>>>(
          DATACENTER_ENDPOINTS.BASE,
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
      queryKey: ['datacenters', 'search', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<Datacenter>>>(
          DATACENTER_ENDPOINTS.SEARCH,
          {
            params: {
              keyword: params.searchTerm,
              page: params.page,
              size: params.size,
              sortBy: params.sortBy || 'datacenterName',
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
      mutationFn: async (data: DatacenterFormValues) => {
        const response = await api.post<ApiResponse<Datacenter>>(
          DATACENTER_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['datacenters'] });
        toast.success('Datacenter created successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to create datacenter');
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: DatacenterFormValues }) => {
        const response = await api.put<ApiResponse<Datacenter>>(
          DATACENTER_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['datacenters'] });
        toast.success('Datacenter updated successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to update datacenter');
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(DATACENTER_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['datacenters'] });
        toast.success('Datacenter deleted successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to delete datacenter');
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }): Promise<FlatPageResponse<Datacenter>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Datacenter>>>(
      DATACENTER_ENDPOINTS.BASE,
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

  getList: async (): Promise<Datacenter[]> => {
    const response = await api.get<ApiResponse<Datacenter[]>>(
      DATACENTER_ENDPOINTS.LIST
    );
    return response.data.data;
  },
};
