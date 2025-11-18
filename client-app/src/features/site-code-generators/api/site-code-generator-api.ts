import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { type BackendPageResponse, type FlatPageResponse, flattenPageResponse } from "@/lib/api-utils";
import type { SiteCodeGenerator, SiteCodeGeneratorFormData, GeneratedSiteCode } from "./schema";

const SITE_CODE_GENERATOR_ENDPOINTS = {
  BASE: "/api/site-code-generators",
  SEARCH: "/api/site-code-generators/search",
  LIST: "/api/site-code-generators/list",
  BY_ID: (id: number) => `/api/site-code-generators/${id}`,
  GENERATE: "/api/site-code-generators/generate",
  PREVIEW: "/api/site-code-generators/preview",
};

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

export const siteCodeGeneratorApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["site-code-generators", params],
      queryFn: async () => {
        const endpoint = params.search ? SITE_CODE_GENERATOR_ENDPOINTS.SEARCH : SITE_CODE_GENERATOR_ENDPOINTS.BASE;
        const response = await api.get<ApiResponse<BackendPageResponse<SiteCodeGenerator>>>(
          endpoint,
          {
            params: {
              page: params.page,
              size: params.size,
              sortBy: params.sortBy || "id",
              direction: params.sortOrder || "asc",
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
      mutationFn: async (data: SiteCodeGeneratorFormData) => {
        const response = await api.post<ApiResponse<SiteCodeGenerator>>(
          SITE_CODE_GENERATOR_ENDPOINTS.BASE,
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["site-code-generators"] });
        toast.success("Site code generator created successfully");
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || "Failed to create generator";
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: SiteCodeGeneratorFormData }) => {
        const response = await api.put<ApiResponse<SiteCodeGenerator>>(
          SITE_CODE_GENERATOR_ENDPOINTS.BY_ID(id),
          data
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["site-code-generators"] });
        toast.success("Site code generator updated successfully");
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || "Failed to update generator";
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(SITE_CODE_GENERATOR_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["site-code-generators"] });
        toast.success("Site code generator deleted successfully");
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || "Failed to delete generator";
        toast.error(message);
      },
    });
  },

  useGenerateCode: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (params: { projectId: number; stateId: number }) => {
        const response = await api.post<ApiResponse<GeneratedSiteCode>>(
          SITE_CODE_GENERATOR_ENDPOINTS.GENERATE,
          null,
          { params }
        );
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["site-code-generators"] });
        toast.success("Site code generated successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to generate site code";
        toast.error(message);
      },
    });
  },

  usePreviewCode: () => {
    return useMutation({
      mutationFn: async (params: { projectId: number; stateId: number }) => {
        const response = await api.get<ApiResponse<GeneratedSiteCode>>(
          SITE_CODE_GENERATOR_ENDPOINTS.PREVIEW,
          { params }
        );
        return response.data.data;
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to preview site code";
        toast.error(message);
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    direction: string;
    searchTerm?: string;
  }): Promise<FlatPageResponse<SiteCodeGenerator>> => {
    const endpoint = params.searchTerm ? SITE_CODE_GENERATOR_ENDPOINTS.SEARCH : SITE_CODE_GENERATOR_ENDPOINTS.BASE;
    const response = await api.get<ApiResponse<BackendPageResponse<SiteCodeGenerator>>>(
      endpoint,
      { params }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<SiteCodeGenerator[]> => {
    const response = await api.get<ApiResponse<SiteCodeGenerator[]>>(SITE_CODE_GENERATOR_ENDPOINTS.LIST);
    return response.data.data;
  },
};
