import api from "@/lib/api";
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';
import type { SiteType, SiteTypeFormData } from "./schema";

const SITE_TYPE_ENDPOINTS = {
  BASE: "/api/site-types",
  SEARCH: "/api/site-types/search",
  LIST: "/api/site-types/list",
  BY_ID: (id: number) => `/api/site-types/${id}`,
};



interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp?: string;
}

export const siteTypeApi = {
  create: async (data: SiteTypeFormData): Promise<SiteType> => {
    const response = await api.post<ApiResponse<SiteType>>(SITE_TYPE_ENDPOINTS.BASE, data);
    return response.data.data; // Unwrap ApiResponse
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
  }): Promise<FlatPageResponse<SiteType>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<SiteType>>>(SITE_TYPE_ENDPOINTS.BASE, { params });
    return flattenPageResponse(response.data.data); // Unwrap ApiResponse
  },

  search: async (params: {
    searchTerm: string;
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
  }): Promise<FlatPageResponse<SiteType>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<SiteType>>>(SITE_TYPE_ENDPOINTS.SEARCH, { params });
    return flattenPageResponse(response.data.data); // Unwrap ApiResponse
  },

  getList: async (): Promise<SiteType[]> => {
    const response = await api.get<ApiResponse<SiteType[]>>(SITE_TYPE_ENDPOINTS.LIST);
    return response.data.data; // Unwrap ApiResponse
  },

  getById: async (id: number): Promise<SiteType> => {
    const response = await api.get<ApiResponse<SiteType>>(SITE_TYPE_ENDPOINTS.BY_ID(id));
    return response.data.data; // Unwrap ApiResponse
  },

  update: async (id: number, data: SiteTypeFormData): Promise<SiteType> => {
    const response = await api.put<ApiResponse<SiteType>>(SITE_TYPE_ENDPOINTS.BY_ID(id), data);
    return response.data.data; // Unwrap ApiResponse
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(SITE_TYPE_ENDPOINTS.BY_ID(id));
  },
};
