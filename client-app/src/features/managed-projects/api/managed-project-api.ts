import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { handleServerError } from "@/lib/handle-server-error";
import type { ManagedProject, ManagedProjectFormData } from "./schema";

const MANAGED_PROJECT_ENDPOINTS = {
  BASE: "/api/managed-projects",
  SEARCH: "/api/managed-projects/search",
  LIST: "/api/managed-projects/list",
  BY_BANK: (bankId: number) => `/api/managed-projects/bank/${bankId}`,
  BY_ID: (id: number) => `/api/managed-projects/${id}`,
};

interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export const managedProjectApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["managed-projects", params],
      queryFn: async () => {
        const response = await api.get<PagedResponse<ManagedProject>>(
          MANAGED_PROJECT_ENDPOINTS.BASE,
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
        return response.data;
      },
    });
  },

  useGetByBank: (
    bankId: number | null,
    params: {
      page: number;
      size: number;
      sortBy?: string;
      sortOrder?: string;
    }
  ) => {
    return useQuery({
      queryKey: ["managed-projects", "by-bank", bankId, params],
      queryFn: async () => {
        if (!bankId) return null;
        const response = await api.get<PagedResponse<ManagedProject>>(
          MANAGED_PROJECT_ENDPOINTS.BY_BANK(bankId),
          {
            params: {
              page: params.page,
              size: params.size,
              ...(params.sortBy && { sortBy: params.sortBy }),
              ...(params.sortOrder && { sortDirection: params.sortOrder }),
            },
          }
        );
        return response.data;
      },
      enabled: !!bankId,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: ManagedProjectFormData) => {
        const response = await api.post<ManagedProject>(MANAGED_PROJECT_ENDPOINTS.BASE, data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["managed-projects"] });
        toast.success("Managed project created successfully");
      },
      onError: (error) => {
        handleServerError(error);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: ManagedProjectFormData }) => {
        const response = await api.put<ManagedProject>(MANAGED_PROJECT_ENDPOINTS.BY_ID(id), data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["managed-projects"] });
        toast.success("Managed project updated successfully");
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
        await api.delete(MANAGED_PROJECT_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["managed-projects"] });
        toast.success("Managed project deleted successfully");
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
  }): Promise<PagedResponse<ManagedProject>> => {
    const response = await api.get<PagedResponse<ManagedProject>>(
      params.searchTerm ? MANAGED_PROJECT_ENDPOINTS.SEARCH : MANAGED_PROJECT_ENDPOINTS.BASE,
      { params }
    );
    return response.data;
  },

  getList: async (): Promise<ManagedProject[]> => {
    const response = await api.get(MANAGED_PROJECT_ENDPOINTS.LIST);
    return response.data;
  },
};
