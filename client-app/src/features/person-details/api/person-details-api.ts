import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from "@/lib/api-utils";
import type { PersonDetails, PersonDetailsFormData } from "./schema";

const PERSON_DETAILS_ENDPOINTS = {
  BASE: "/api/person-details",
  SEARCH: "/api/person-details/search",
  LIST: "/api/person-details/list",
  BY_PERSON_TYPE: (personTypeId: number) => `/api/person-details/person-type/${personTypeId}`,
  BY_ID: (id: number) => `/api/person-details/${id}`,
};

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

export const personDetailsApi = {
  useGetAll: (params: {
    page: number;
    size: number;
    sortBy?: string;
    sortOrder?: string;
    search?: string;
  }) => {
    return useQuery({
      queryKey: ["person-details", params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<PersonDetails>>>(
          PERSON_DETAILS_ENDPOINTS.BASE,
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

  useGetByPersonType: (
    personTypeId: number | null,
    params: {
      page: number;
      size: number;
      sortBy?: string;
      sortOrder?: string;
    }
  ) => {
    return useQuery({
      queryKey: ["person-details", "by-person-type", personTypeId, params],
      queryFn: async () => {
        if (!personTypeId) return null;
        const response = await api.get<ApiResponse<BackendPageResponse<PersonDetails>>>(
          PERSON_DETAILS_ENDPOINTS.BY_PERSON_TYPE(personTypeId),
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
      enabled: !!personTypeId,
    });
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ["person-details", "search", searchTerm],
      queryFn: async () => {
        const endpoint = searchTerm?.trim() ? PERSON_DETAILS_ENDPOINTS.SEARCH : PERSON_DETAILS_ENDPOINTS.BASE;
        const params: Record<string, unknown> = {
          page: 0,
          size: 20,
          sortBy: "firstName",
          sortDirection: "ASC",
        };
        
        if (searchTerm?.trim()) {
          params.searchTerm = searchTerm.trim();
        }
        
        const response = await api.get<ApiResponse<BackendPageResponse<PersonDetails>>>(
          endpoint,
          { params }
        );
        return flattenPageResponse(response.data.data).content;
      },
      staleTime: 30000,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: PersonDetailsFormData) => {
        const response = await api.post<ApiResponse<PersonDetails>>(PERSON_DETAILS_ENDPOINTS.BASE, data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["person-details"] });
        toast.success("Person details created successfully");
      },
      onError: (error: any) => {
        const errorMessage = error?.response?.data?.message || "Failed to create person details";
        toast.error(errorMessage);
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: PersonDetailsFormData }) => {
        const response = await api.put<ApiResponse<PersonDetails>>(PERSON_DETAILS_ENDPOINTS.BY_ID(id), data);
        return response.data.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["person-details"] });
        toast.success("Person details updated successfully");
      },
      onError: (error: any) => {
        const errorMessage = error?.response?.data?.message || "Failed to update person details";
        toast.error(errorMessage);
      },
    });
  },

  useDelete: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (id: number) => {
        await api.delete(PERSON_DETAILS_ENDPOINTS.BY_ID(id));
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["person-details"] });
        toast.success("Person details deleted successfully");
      },
      onError: (error: any) => {
        // Extract the error message from the backend response
        const errorMessage = error?.response?.data?.message || "Failed to delete person details";
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
  }): Promise<FlatPageResponse<PersonDetails>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<PersonDetails>>>(
      params.searchTerm ? PERSON_DETAILS_ENDPOINTS.SEARCH : PERSON_DETAILS_ENDPOINTS.BASE,
      { params }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<PersonDetails[]> => {
    const response = await api.get<ApiResponse<PersonDetails[]>>(PERSON_DETAILS_ENDPOINTS.LIST);
    return response.data.data;
  },
};
