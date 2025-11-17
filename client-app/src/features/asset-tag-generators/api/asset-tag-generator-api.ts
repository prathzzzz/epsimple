import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { type BackendPageResponse, type FlatPageResponse, flattenPageResponse } from "@/lib/api-utils";
import type { AssetTagCodeGenerator, AssetTagCodeGeneratorFormData, GeneratedAssetTag } from "./schema";

const ASSET_TAG_GENERATOR_ENDPOINTS = {
  BASE: "/api/asset-tag-generators",
  SEARCH: "/api/asset-tag-generators/search",
  LIST: "/api/asset-tag-generators/list",
  BY_ID: (id: number) => `/api/asset-tag-generators/${id}`,
  GENERATE: "/api/asset-tag-generators/generate",
  PREVIEW: "/api/asset-tag-generators/preview",
};

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

export const assetTagCodeGeneratorApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["asset-tag-generators", params],
      queryFn: async () => {
        const endpoint = params.search ? ASSET_TAG_GENERATOR_ENDPOINTS.SEARCH : ASSET_TAG_GENERATOR_ENDPOINTS.BASE;
        const response = await api.get<ApiResponse<BackendPageResponse<AssetTagCodeGenerator>>>(
          endpoint,
          {
            params: {
              page: params.page,
              size: params.size,
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
      mutationFn: async (data: AssetTagCodeGeneratorFormData) => {
        const response = await api.post<ApiResponse<AssetTagCodeGenerator>>(
          ASSET_TAG_GENERATOR_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["asset-tag-generators"] });
        toast.success("Asset tag generator created successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to create asset tag generator";
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: AssetTagCodeGeneratorFormData }) => {
        const response = await api.put<ApiResponse<AssetTagCodeGenerator>>(
          ASSET_TAG_GENERATOR_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["asset-tag-generators"] });
        toast.success("Asset tag generator updated successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to update asset tag generator";
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(ASSET_TAG_GENERATOR_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["asset-tag-generators"] });
        toast.success("Asset tag generator deleted successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to delete asset tag generator";
        toast.error(message);
      },
    });
  },

  useGenerateTag: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (params: { assetCategoryId: number; vendorId: number; bankId: number }) => {
        const response = await api.post<ApiResponse<GeneratedAssetTag>>(
          ASSET_TAG_GENERATOR_ENDPOINTS.GENERATE,
          params
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["asset-tag-generators"] });
        toast.success("Asset tag generated successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to generate asset tag";
        toast.error(message);
      },
    });
  },

  usePreviewTag: () => {
    return useMutation({
      mutationFn: async (params: { assetCategoryId: number; vendorId: number; bankId: number }) => {
        const response = await api.get<ApiResponse<GeneratedAssetTag>>(
          ASSET_TAG_GENERATOR_ENDPOINTS.PREVIEW,
          { params }
        );
        return response.data.data;
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to preview asset tag";
        toast.error(message);
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    searchTerm?: string;
  }): Promise<FlatPageResponse<AssetTagCodeGenerator>> => {
    const endpoint = params.searchTerm ? ASSET_TAG_GENERATOR_ENDPOINTS.SEARCH : ASSET_TAG_GENERATOR_ENDPOINTS.BASE;
    const response = await api.get<ApiResponse<BackendPageResponse<AssetTagCodeGenerator>>>(
      endpoint,
      { params }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<AssetTagCodeGenerator[]> => {
    const response = await api.get<ApiResponse<AssetTagCodeGenerator[]>>(ASSET_TAG_GENERATOR_ENDPOINTS.LIST);
    return response.data.data;
  },
};
