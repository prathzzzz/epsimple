import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { flattenPageResponse, type BackendPageResponse } from '@/lib/api-utils';
import { toast } from 'sonner';
import type { AssetExpenditureAndActivityWorkFormData, AssetExpenditureAndActivityWork } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const EXPENDITURE_ENDPOINTS = {
  BASE: '/api/asset-expenditure-and-activity-works',
  BY_ID: (id: number) => `/api/asset-expenditure-and-activity-works/${id}`,
  BY_ASSET: (assetId: number) => `/api/asset-expenditure-and-activity-works/asset/${assetId}`,
  BY_ACTIVITY_WORK: (activityWorkId: number) => `/api/asset-expenditure-and-activity-works/activity-work/${activityWorkId}`,
  SEARCH: '/api/asset-expenditure-and-activity-works/search',
};

export const assetExpenditureAndActivityWorkApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
  }) => {
    return useQuery({
      queryKey: ['asset-expenditure-and-activity-works', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetExpenditureAndActivityWork>>>(
          EXPENDITURE_ENDPOINTS.BASE,
          {
            params: {
              page: params.page,
              size: params.size,
              ...(params.sortBy && { sortBy: params.sortBy }),
              ...(params.sortOrder && { sortDirection: params.sortOrder }),
            },
          }
        );
        return flattenPageResponse(response.data.data);
      },
    });
  },

  useSearch: (keyword: string) => {
    return useQuery({
      queryKey: ['asset-expenditure-and-activity-works', 'search', keyword],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetExpenditureAndActivityWork>>>(
          EXPENDITURE_ENDPOINTS.SEARCH,
          {
            params: {
              keyword,
              page: 0,
              size: 100,
            },
          }
        );
        return flattenPageResponse(response.data.data);
      },
      enabled: keyword.length >= 2,
    });
  },

  useGetByAssetId: (assetId: number, params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
  }) => {
    return useQuery({
      queryKey: ['asset-expenditure-and-activity-works', 'asset', assetId, params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetExpenditureAndActivityWork>>>(
          EXPENDITURE_ENDPOINTS.BY_ASSET(assetId),
          {
            params: {
              page: params.page,
              size: params.size,
              ...(params.sortBy && { sortBy: params.sortBy }),
              ...(params.sortOrder && { sortDirection: params.sortOrder }),
            },
          }
        );
        return flattenPageResponse(response.data.data);
      },
      enabled: !!assetId,
    });
  },

  useGetByActivityWorkId: (activityWorkId: number, params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
  }) => {
    return useQuery({
      queryKey: ['asset-expenditure-and-activity-works', 'activity-work', activityWorkId, params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetExpenditureAndActivityWork>>>(
          EXPENDITURE_ENDPOINTS.BY_ACTIVITY_WORK(activityWorkId),
          {
            params: {
              page: params.page,
              size: params.size,
              ...(params.sortBy && { sortBy: params.sortBy }),
              ...(params.sortOrder && { sortDirection: params.sortOrder }),
            },
          }
        );
        return flattenPageResponse(response.data.data);
      },
      enabled: !!activityWorkId,
    });
  },

  useGetById: (id: number) => {
    return useQuery({
      queryKey: ['asset-expenditure-and-activity-works', id],
      queryFn: async () => {
        const response = await api.get<ApiResponse<AssetExpenditureAndActivityWork>>(
          EXPENDITURE_ENDPOINTS.BY_ID(id)
        );
        return response.data.data;
      },
      enabled: !!id,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: AssetExpenditureAndActivityWorkFormData) => {
        const response = await api.post<ApiResponse<AssetExpenditureAndActivityWork>>(
          EXPENDITURE_ENDPOINTS.BASE,
          data
        );
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['asset-expenditure-and-activity-works'] });
        toast.success('Asset expenditure created successfully');
      },
      onError: (error: Error | unknown) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error as any).response?.data?.message || 'Failed to create asset expenditure';
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: AssetExpenditureAndActivityWorkFormData }) => {
        const response = await api.put<ApiResponse<AssetExpenditureAndActivityWork>>(
          EXPENDITURE_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['asset-expenditure-and-activity-works'] });
        toast.success('Asset expenditure updated successfully');
      },
      onError: (error: Error | unknown) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error as any).response?.data?.message || 'Failed to update asset expenditure';
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        const response = await api.delete<ApiResponse<void>>(
          EXPENDITURE_ENDPOINTS.BY_ID(id)
        );
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['asset-expenditure-and-activity-works'] });
        toast.success('Asset expenditure deleted successfully');
      },
      onError: (error: Error | unknown) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error as any).response?.data?.message || 'Failed to delete asset expenditure';
        toast.error(message);
      },
    });
  },
};
