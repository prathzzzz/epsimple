import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import { type BackendPageResponse, type FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { ActivityWork, ActivityWorkFormData } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const ACTIVITY_WORK_ENDPOINTS = {
  BASE: '/api/activity-works',
  SEARCH: '/api/activity-works/search',
  LIST: '/api/activity-works/list',
  BY_ID: (id: number) => `/api/activity-works/${id}`,
};

export const activityWorkApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['activity-works', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<ActivityWork>>>(
          ACTIVITY_WORK_ENDPOINTS.BASE,
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
      queryKey: ['activity-works', 'search', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<ActivityWork>>>(
          ACTIVITY_WORK_ENDPOINTS.SEARCH,
          {
            params: {
              keyword: params.searchTerm,
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
      mutationFn: async (data: ActivityWorkFormData) => {
        const response = await api.post<ApiResponse<ActivityWork>>(
          ACTIVITY_WORK_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['activity-works'] });
        toast.success('Activity work created successfully');
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to create activity work';
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: ActivityWorkFormData }) => {
        const response = await api.put<ApiResponse<ActivityWork>>(
          ACTIVITY_WORK_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['activity-works'] });
        toast.success('Activity work updated successfully');
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to update activity work';
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(ACTIVITY_WORK_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['activity-works'] });
        toast.success('Activity work deleted successfully');
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : 'Failed to delete activity work';
        toast.error(message);
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }): Promise<FlatPageResponse<ActivityWork>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<ActivityWork>>>(
      ACTIVITY_WORK_ENDPOINTS.BASE,
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

  getList: async (): Promise<ActivityWork[]> => {
    const response = await api.get<ApiResponse<ActivityWork[]>>(
      ACTIVITY_WORK_ENDPOINTS.LIST
    );
    return response.data.data;
  },
};
