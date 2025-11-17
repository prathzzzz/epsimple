import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { type BackendPageResponse, type FlatPageResponse, flattenPageResponse } from "@/lib/api-utils";
import type { City, CityFormData } from "./schema";

const CITY_ENDPOINTS = {
  BASE: "/api/cities",
  SEARCH: "/api/cities/search",
  LIST: "/api/cities/list",
  BY_STATE: (stateId: number) => `/api/cities/state/${stateId}`,
  BY_ID: (id: number) => `/api/cities/${id}`,
};

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

export const cityApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["cities", params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<City>>>(
          CITY_ENDPOINTS.BASE,
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

  useGetByState: (
    stateId: number | null,
    params: {
      page: number;
      size: number;
      sortBy?: string;
      sortOrder?: string;
    }
  ) => {
    return useQuery({
      queryKey: ["cities", "by-state", stateId, params],
      queryFn: async () => {
        if (!stateId) return null;
        const response = await api.get<ApiResponse<BackendPageResponse<City>>>(
          CITY_ENDPOINTS.BY_STATE(stateId),
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
      enabled: !!stateId,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: CityFormData) => {
        const response = await api.post<ApiResponse<City>>(CITY_ENDPOINTS.BASE, data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["cities"] });
        toast.success("City created successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to create city";
        toast.error(message);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: CityFormData }) => {
        const response = await api.put<ApiResponse<City>>(CITY_ENDPOINTS.BY_ID(id), data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["cities"] });
        toast.success("City updated successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to update city";
        toast.error(message);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(CITY_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["cities"] });
        toast.success("City deleted successfully");
      },
      onError: (error: unknown) => {
        const message = error instanceof Error ? error.message : "Failed to delete city";
        toast.error(message);
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
    searchTerm?: string;
  }): Promise<FlatPageResponse<City>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<City>>>(
      params.searchTerm ? CITY_ENDPOINTS.SEARCH : CITY_ENDPOINTS.BASE,
      { params }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<City[]> => {
    const response = await api.get<ApiResponse<City[]>>(CITY_ENDPOINTS.LIST);
    return response.data.data;
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['cities', 'search', searchTerm],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<City>>>(
          searchTerm?.trim() ? CITY_ENDPOINTS.SEARCH : CITY_ENDPOINTS.BASE,
          {
            params: {
              ...(searchTerm?.trim() && { searchTerm: searchTerm.trim() }),
              page: 0,
              size: 20,
              sortBy: 'cityName',
              sortDirection: 'ASC',
            },
          }
        );
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },
};
