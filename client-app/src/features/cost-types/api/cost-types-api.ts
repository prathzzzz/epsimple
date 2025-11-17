import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { type BackendPageResponse, type FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';
import { handleServerError } from "@/lib/handle-server-error";

export interface CostType {
  id: number;
  typeName: string;
  typeDescription: string | null;
  costCategoryId: number;
  costCategoryName: string;
  createdAt: string;
  updatedAt: string;
}

export interface CostTypeFormData {
  typeName: string;
  typeDescription?: string;
  costCategoryId: number;
}



interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const COST_TYPE_ENDPOINTS = {
  BASE: "/api/cost-types",
  SEARCH: "/api/cost-types/search",
  LIST: "/api/cost-types/list",
  BY_ID: (id: number) => `/api/cost-types/${id}`,
};

export const costTypesApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["cost-types", params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<CostType>>>(
          COST_TYPE_ENDPOINTS.BASE,
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

  useGetList: () => {
    return useQuery({
      queryKey: ["cost-types", "list"],
      queryFn: async () => {
        const response = await api.get<ApiResponse<CostType[]>>(COST_TYPE_ENDPOINTS.LIST);
        return response.data.data;
      },
    });
  },

  useGetById: (id: number | null) => {
    return useQuery({
      queryKey: ["cost-types", id],
      queryFn: async () => {
        if (!id) return null;
        const response = await api.get<ApiResponse<CostType>>(COST_TYPE_ENDPOINTS.BY_ID(id));
        return response.data.data;
      },
      enabled: !!id,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: CostTypeFormData) => {
        const response = await api.post<ApiResponse<CostType>>(COST_TYPE_ENDPOINTS.BASE, data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["cost-types"] });
        toast.success("Cost type created successfully");
      },
      onError: (error) => {
        handleServerError(error);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: CostTypeFormData }) => {
        const response = await api.put<ApiResponse<CostType>>(COST_TYPE_ENDPOINTS.BY_ID(id), data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["cost-types"] });
        toast.success("Cost type updated successfully");
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
        await api.delete<ApiResponse<void>>(COST_TYPE_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["cost-types"] });
        toast.success("Cost type deleted successfully");
      },
      onError: (error) => {
        handleServerError(error);
      },
    });
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['cost-types', 'search', searchTerm],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<CostType>>>(
          searchTerm?.trim() ? COST_TYPE_ENDPOINTS.SEARCH : COST_TYPE_ENDPOINTS.BASE,
          {
            params: {
              ...(searchTerm?.trim() && { searchTerm: searchTerm.trim() }),
              page: 0,
              size: 20,
              sortBy: 'typeName',
              sortDirection: 'ASC',
            },
          }
        );
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
    searchTerm?: string;
  }): Promise<FlatPageResponse<CostType>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<CostType>>>(
      params.searchTerm ? COST_TYPE_ENDPOINTS.SEARCH : COST_TYPE_ENDPOINTS.BASE,
      { params }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<CostType[]> => {
    const response = await api.get<ApiResponse<CostType[]>>(COST_TYPE_ENDPOINTS.LIST);
    return response.data.data;
  },
};
