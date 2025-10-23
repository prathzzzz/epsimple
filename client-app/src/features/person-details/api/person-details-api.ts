import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import api from "@/lib/api";
import type { PersonDetails, PersonDetailsFormData } from "./schema";

const PERSON_DETAILS_ENDPOINTS = {
  BASE: "/api/person-details",
  SEARCH: "/api/person-details/search",
  LIST: "/api/person-details/list",
  BY_PERSON_TYPE: (personTypeId: number) => `/api/person-details/person-type/${personTypeId}`,
  BY_ID: (id: number) => `/api/person-details/${id}`,
};

interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
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
        const response = await api.get<PagedResponse<PersonDetails>>(
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
        return response.data;
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
        const response = await api.get<PagedResponse<PersonDetails>>(
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
        return response.data;
      },
      enabled: !!personTypeId,
    });
  },

  useCreate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async (data: PersonDetailsFormData) => {
        const response = await api.post<PersonDetails>(PERSON_DETAILS_ENDPOINTS.BASE, data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["person-details"] });
        toast.success("Person details created successfully");
      },
      onError: () => {
        toast.error("Failed to create person details");
      },
    });
  },

  useUpdate: () => {
    const queryClient = useQueryClient();
    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: PersonDetailsFormData }) => {
        const response = await api.put<PersonDetails>(PERSON_DETAILS_ENDPOINTS.BY_ID(id), data);
        return response.data;
      },
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["person-details"] });
        toast.success("Person details updated successfully");
      },
      onError: () => {
        toast.error("Failed to update person details");
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
      onError: () => {
        toast.error("Failed to delete person details");
      },
    });
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
    searchTerm?: string;
  }): Promise<PagedResponse<PersonDetails>> => {
    const response = await api.get<PagedResponse<PersonDetails>>(
      params.searchTerm ? PERSON_DETAILS_ENDPOINTS.SEARCH : PERSON_DETAILS_ENDPOINTS.BASE,
      { params }
    );
    return response.data;
  },

  getList: async (): Promise<PersonDetails[]> => {
    const response = await api.get(PERSON_DETAILS_ENDPOINTS.LIST);
    return response.data;
  },
};
