import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import type { AssetCategory, AssetCategoryFormData } from "./schema";

const ASSET_CATEGORY_ENDPOINTS = {
  BASE: "/api/asset-categories",
  SEARCH: "/api/asset-categories/search",
  LIST: "/api/asset-categories/list",
  BY_ID: (id: number) => `/api/asset-categories/${id}`,
};

interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp?: string;
}

export const assetCategoryApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["asset-categories", params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<PagedResponse<AssetCategory>>>(
          ASSET_CATEGORY_ENDPOINTS.BASE,
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
        return response.data.data; // Unwrap the ApiResponse wrapper
      },
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: AssetCategoryFormData) => {
        const response = await api.post<ApiResponse<AssetCategory>>(
          ASSET_CATEGORY_ENDPOINTS.BASE,
          data
        );
        return response.data.data; // Unwrap the ApiResponse wrapper
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["asset-categories"] });
        toast.success("Asset category created successfully");
      },
      onError: () => {
        toast.error("Failed to create asset category");
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
        data: AssetCategoryFormData;
      }) => {
        const response = await api.put<ApiResponse<AssetCategory>>(
          ASSET_CATEGORY_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data; // Unwrap the ApiResponse wrapper
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["asset-categories"] });
        toast.success("Asset category updated successfully");
      },
      onError: () => {
        toast.error("Failed to update asset category");
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(ASSET_CATEGORY_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["asset-categories"] });
        toast.success("Asset category deleted successfully");
      },
      onError: () => {
        toast.error("Failed to delete asset category");
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
    searchTerm?: string;
  }): Promise<PagedResponse<AssetCategory>> => {
    const response = await api.get<ApiResponse<PagedResponse<AssetCategory>>>(
      params.searchTerm
        ? ASSET_CATEGORY_ENDPOINTS.SEARCH
        : ASSET_CATEGORY_ENDPOINTS.BASE,
      { params }
    );
    return response.data.data; // Unwrap the ApiResponse wrapper
  },

  getList: async (): Promise<AssetCategory[]> => {
    const response = await api.get<ApiResponse<AssetCategory[]>>(ASSET_CATEGORY_ENDPOINTS.LIST);
    return response.data.data; // Unwrap the ApiResponse wrapper
  },
};
