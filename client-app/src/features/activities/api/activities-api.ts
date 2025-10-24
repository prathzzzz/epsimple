import api from "@/lib/api";

import { BackendPageResponse } from '@/lib/api-utils';
export interface Activity {
  id: number;
  activityName: string;
  activityDescription: string | null;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface ActivityFormData {
  activityName: string;
  activityDescription?: string;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/activity";

export const activitiesApi = {
  create: async (data: ActivityFormData): Promise<ApiResponse<Activity>> => {
    const response = await api.post<ApiResponse<Activity>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<BackendPageResponse<Activity>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Activity>>>(BASE_URL, {
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
  ): Promise<ApiResponse<BackendPageResponse<Activity>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<Activity>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<Activity[]>> => {
    const response = await api.get<ApiResponse<Activity[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<Activity>> => {
    const response = await api.get<ApiResponse<Activity>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: ActivityFormData): Promise<ApiResponse<Activity>> => {
    const response = await api.put<ApiResponse<Activity>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },
};
