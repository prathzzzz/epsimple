import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from "@/lib/api-utils";
import { handleServerError } from "@/lib/handle-server-error";
import type { Site, SiteFormData } from "./schema";

const SITE_ENDPOINTS = {
  BASE: "/api/sites",
  SEARCH: "/api/sites/search",
  LIST: "/api/sites/list",
  BY_ID: (id: number) => `/api/sites/${id}`,
};

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

export const siteApi = {
  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['sites', 'search', searchTerm],
      queryFn: async () => {
        if (!searchTerm || searchTerm.trim().length === 0) return [];
        const response = await api.get<ApiResponse<BackendPageResponse<Site>>>(
          SITE_ENDPOINTS.SEARCH,
          {
            params: {
              searchTerm: searchTerm.trim(),
              page: 0,
              size: 50,
              sortBy: 'siteName',
              sortDirection: 'asc',
            },
          }
        );
        return flattenPageResponse(response.data.data).content;
      },
      enabled: searchTerm.trim().length > 0,
      staleTime: 30000,
    });
  },

  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["sites", params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<Site>>>(
          SITE_ENDPOINTS.BASE,
          {
            params: {
              page: params.page,
              size: params.size,
              ...(params.sortBy && { sortBy: params.sortBy }),
              ...(params.sortOrder && { sortDirection: params.sortOrder }),
              ...(params.search && { searchTerm: params.search }),
            },
          }
        );
        return flattenPageResponse(response.data.data);
      },
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: SiteFormData) => {
        const response = await api.post<ApiResponse<Site>>(SITE_ENDPOINTS.BASE, data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["sites"] });
        toast.success("Site created successfully");
      },
      onError: (error) => {
        handleServerError(error);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: SiteFormData }) => {
        const response = await api.put<ApiResponse<Site>>(SITE_ENDPOINTS.BY_ID(id), data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["sites"] });
        toast.success("Site updated successfully");
      },
      onError: (error) => {
        handleServerError(error);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(SITE_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["sites"] });
        toast.success("Site deleted successfully");
      },
      onError: (error) => {
        handleServerError(error);
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
    searchTerm?: string;
  }): Promise<FlatPageResponse<Site>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Site>>>(
      params.searchTerm ? SITE_ENDPOINTS.SEARCH : SITE_ENDPOINTS.BASE,
      { params }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<Site[]> => {
    const response = await api.get<ApiResponse<Site[]>>(SITE_ENDPOINTS.LIST);
    return response.data.data;
  },
};
