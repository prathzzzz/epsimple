import api from "@/lib/api";

export interface ActivitiesList {
  id: number;
  activityId: number;
  activityName: string;
  activityCategory: string | null;
  activityDescription: string | null;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface ActivitiesListFormData {
  activityId: number;
  activityName: string;
  activityCategory?: string;
  activityDescription?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/activities";

export const activitiesListApi = {
  create: async (data: ActivitiesListFormData): Promise<ApiResponse<ActivitiesList>> => {
    const response = await api.post<ApiResponse<ActivitiesList>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<PageResponse<ActivitiesList>>> => {
    const response = await api.get<ApiResponse<PageResponse<ActivitiesList>>>(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  search: async (
    searchTerm: string,
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<PageResponse<ActivitiesList>>> => {
    const response = await api.get<ApiResponse<PageResponse<ActivitiesList>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<ActivitiesList[]>> => {
    const response = await api.get<ApiResponse<ActivitiesList[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<ActivitiesList>> => {
    const response = await api.get<ApiResponse<ActivitiesList>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: ActivitiesListFormData): Promise<ApiResponse<ActivitiesList>> => {
    const response = await api.put<ApiResponse<ActivitiesList>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },
};
