import api from "@/lib/api";
import type { SiteType, SiteTypeFormData } from "./schema";

const SITE_TYPE_ENDPOINTS = {
  BASE: "/api/site-types",
  SEARCH: "/api/site-types/search",
  LIST: "/api/site-types/list",
  BY_ID: (id: number) => `/api/site-types/${id}`,
};

export const siteTypeApi = {
  create: async (data: SiteTypeFormData): Promise<SiteType> => {
    const response = await api.post(SITE_TYPE_ENDPOINTS.BASE, data);
    return response.data;
  },

  getAll: async (params: {
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
  }) => {
    const response = await api.get(SITE_TYPE_ENDPOINTS.BASE, { params });
    return response.data;
  },

  search: async (params: {
    searchTerm: string;
    page: number;
    size: number;
    sortBy: string;
    sortDirection: string;
  }) => {
    const response = await api.get(SITE_TYPE_ENDPOINTS.SEARCH, { params });
    return response.data;
  },

  getList: async (): Promise<SiteType[]> => {
    const response = await api.get(SITE_TYPE_ENDPOINTS.LIST);
    return response.data;
  },

  getById: async (id: number): Promise<SiteType> => {
    const response = await api.get(SITE_TYPE_ENDPOINTS.BY_ID(id));
    return response.data;
  },

  update: async (id: number, data: SiteTypeFormData): Promise<SiteType> => {
    const response = await api.put(SITE_TYPE_ENDPOINTS.BY_ID(id), data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(SITE_TYPE_ENDPOINTS.BY_ID(id));
  },
};
