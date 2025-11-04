import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { flattenPageResponse, type BackendPageResponse } from '@/lib/api-utils';
import { toast } from 'sonner';
import type { SiteActivityWorkExpenditureFormData, SiteActivityWorkExpenditure } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const EXPENDITURE_ENDPOINTS = {
  BASE: '/api/site-activity-work-expenditures',
  BY_ID: (id: number) => `/api/site-activity-work-expenditures/${id}`,
  BY_SITE: (siteId: number) => `/api/site-activity-work-expenditures/site/${siteId}`,
  BY_ACTIVITY_WORK: (activityWorkId: number) => `/api/site-activity-work-expenditures/activity-work/${activityWorkId}`,
  SEARCH: '/api/site-activity-work-expenditures/search',
};

export const siteActivityWorkExpenditureApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
  }) => {
    return useQuery({
      queryKey: ['site-activity-work-expenditures', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<SiteActivityWorkExpenditure>>>(
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
      queryKey: ['site-activity-work-expenditures', 'search', keyword],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<SiteActivityWorkExpenditure>>>(
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

  useGetBySiteId: (siteId: number, params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
  }) => {
    return useQuery({
      queryKey: ['site-activity-work-expenditures', 'site', siteId, params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<SiteActivityWorkExpenditure>>>(
          EXPENDITURE_ENDPOINTS.BY_SITE(siteId),
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
      enabled: !!siteId,
    });
  },

  useGetByActivityWorkId: (activityWorkId: number, params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
  }) => {
    return useQuery({
      queryKey: ['site-activity-work-expenditures', 'activity-work', activityWorkId, params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<SiteActivityWorkExpenditure>>>(
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
      queryKey: ['site-activity-work-expenditures', id],
      queryFn: async () => {
        const response = await api.get<ApiResponse<SiteActivityWorkExpenditure>>(
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
      mutationFn: async (data: SiteActivityWorkExpenditureFormData) => {
        const response = await api.post<ApiResponse<SiteActivityWorkExpenditure>>(
          EXPENDITURE_ENDPOINTS.BASE,
          data
        );
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['site-activity-work-expenditures'] });
        toast.success('Site activity work expenditure created successfully');
      },
      onError: (error: Error | unknown) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error as any).response?.data?.message || 'Failed to create site activity work expenditure';
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: SiteActivityWorkExpenditureFormData }) => {
        const response = await api.put<ApiResponse<SiteActivityWorkExpenditure>>(
          EXPENDITURE_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['site-activity-work-expenditures'] });
        toast.success('Site activity work expenditure updated successfully');
      },
      onError: (error: Error | unknown) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error as any).response?.data?.message || 'Failed to update site activity work expenditure';
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
        queryClient.invalidateQueries({ queryKey: ['site-activity-work-expenditures'] });
        toast.success('Site activity work expenditure deleted successfully');
      },
      onError: (error: Error | unknown) => {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const message = (error as any).response?.data?.message || 'Failed to delete site activity work expenditure';
        toast.error(message);
      },
    });
  },
};
