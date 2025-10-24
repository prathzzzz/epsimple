import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import type { Location, LocationFormData } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

const LOCATION_ENDPOINTS = {
  BASE: '/api/locations',
  SEARCH: '/api/locations/search',
  LIST: '/api/locations/list',
  BY_ID: (id: number) => `/api/locations/${id}`,
};

export const locationApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['locations', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<PagedResponse<Location>>>(
          LOCATION_ENDPOINTS.BASE,
          {
            params: {
              page: params.page,
              size: params.size,
              sortBy: params.sortBy || 'id',
              sortDirection: params.sortDirection || 'ASC',
            },
          }
        );
        return response.data.data;
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
      queryKey: ['locations', 'search', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<PagedResponse<Location>>>(
          LOCATION_ENDPOINTS.SEARCH,
          {
            params: {
              searchTerm: params.searchTerm,
              page: params.page,
              size: params.size,
              sortBy: params.sortBy || 'locationName',
              sortDirection: params.sortDirection || 'ASC',
            },
          }
        );
        return response.data.data;
      },
      enabled: !!params.searchTerm,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (data: LocationFormData) => {
        const response = await api.post<ApiResponse<Location>>(
          LOCATION_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['locations'] });
        toast.success('Location created successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to create location');
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: LocationFormData }) => {
        const response = await api.put<ApiResponse<Location>>(
          LOCATION_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['locations'] });
        toast.success('Location updated successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to update location');
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(LOCATION_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['locations'] });
        toast.success('Location deleted successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to delete location');
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }): Promise<PagedResponse<Location>> => {
    const response = await api.get<ApiResponse<PagedResponse<Location>>>(
      LOCATION_ENDPOINTS.BASE,
      {
        params: {
          page: params.page,
          size: params.size,
          sortBy: params.sortBy || 'id',
          sortDirection: params.sortDirection || 'ASC',
        },
      }
    );
    return response.data.data;
  },

  getList: async (): Promise<Location[]> => {
    const response = await api.get<ApiResponse<Location[]>>(
      LOCATION_ENDPOINTS.LIST
    );
    return response.data.data;
  },
};
