import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import { BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { AssetsOnSite, AssetsOnSiteFormData } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const ASSETS_ON_SITE_ENDPOINTS = {
  BASE: '/api/assets-on-site',
  SEARCH: '/api/assets-on-site/search',
  BY_SITE: (siteId: number) => `/api/assets-on-site/site/${siteId}`,
  BY_ID: (id: number) => `/api/assets-on-site/${id}`,
};

export const assetsOnSiteApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['assets-on-site', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetsOnSite>>>(
          ASSETS_ON_SITE_ENDPOINTS.BASE,
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
      queryKey: ['assets-on-site', 'search', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetsOnSite>>>(
          ASSETS_ON_SITE_ENDPOINTS.SEARCH,
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

  useBySite: (params: {
    siteId: number;
    page: number;
    size: number;
    sortBy?: string;
    sortDirection?: string;
  }) => {
    return useQuery({
      queryKey: ['assets-on-site', 'site', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<AssetsOnSite>>>(
          ASSETS_ON_SITE_ENDPOINTS.BY_SITE(params.siteId),
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
      enabled: !!params.siteId,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (data: AssetsOnSiteFormData) => {
        const response = await api.post<ApiResponse<AssetsOnSite>>(
          ASSETS_ON_SITE_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-site'] });
        toast.success('Asset placed on site successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to place asset on site');
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: AssetsOnSiteFormData }) => {
        const response = await api.put<ApiResponse<AssetsOnSite>>(
          ASSETS_ON_SITE_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-site'] });
        toast.success('Asset on site updated successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to update asset on site');
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();

    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(ASSETS_ON_SITE_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['assets-on-site'] });
        toast.success('Asset removed from site successfully');
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || 'Failed to remove asset from site');
      },
    });
  },
};
