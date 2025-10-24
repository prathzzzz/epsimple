import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { SiteCategory, SiteCategoryFormData } from "./schema";

const SITE_CATEGORY_ENDPOINTS = {
  BASE: "/api/site-categories",
  SEARCH: "/api/site-categories/search",
  LIST: "/api/site-categories/list",
  BY_ID: (id: number) => `/api/site-categories/${id}`,
};



interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp?: string;
}

export const siteCategoryApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["site-categories", params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<SiteCategory>>>(
          SITE_CATEGORY_ENDPOINTS.BASE,
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
        return flattenPageResponse(response.data.data); // Unwrap ApiResponse
      },
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: SiteCategoryFormData) => {
        const response = await api.post<ApiResponse<SiteCategory>>(
          SITE_CATEGORY_ENDPOINTS.BASE,
          data
        );
        return response.data.data; // Unwrap ApiResponse
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["site-categories"] });
        toast.success("Site category created successfully");
      },
      onError: (error: any) => {
        const errorMessage = error?.response?.data?.message || "Failed to create site category";
        toast.error(errorMessage);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({
        id,
        data,
      }: {
        id: number;
        data: SiteCategoryFormData;
      }) => {
        const response = await api.put<ApiResponse<SiteCategory>>(
          SITE_CATEGORY_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data; // Unwrap ApiResponse
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["site-categories"] });
        toast.success("Site category updated successfully");
      },
      onError: (error: any) => {
        const errorMessage = error?.response?.data?.message || "Failed to update site category";
        toast.error(errorMessage);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(SITE_CATEGORY_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["site-categories"] });
        toast.success("Site category deleted successfully");
      },
      onError: (error: any) => {
        const errorMessage = error?.response?.data?.message || "Failed to delete site category";
        toast.error(errorMessage);
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
    searchTerm?: string;
  }): Promise<FlatPageResponse<SiteCategory>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<SiteCategory>>>(
      params.searchTerm
        ? SITE_CATEGORY_ENDPOINTS.SEARCH
        : SITE_CATEGORY_ENDPOINTS.BASE,
      { params }
    );
    return flattenPageResponse(response.data.data); // Unwrap ApiResponse
  },

  getList: async (): Promise<SiteCategory[]> => {
    const response = await api.get<ApiResponse<SiteCategory[]>>(SITE_CATEGORY_ENDPOINTS.LIST);
    return response.data.data; // Unwrap ApiResponse
  },
};
