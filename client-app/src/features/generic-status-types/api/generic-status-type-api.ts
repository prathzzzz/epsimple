import api from "@/lib/api";

export interface GenericStatusType {
  id: number;
  statusName: string;
  statusCode: string | null;
  description: string | null;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface GenericStatusTypeFormData {
  statusName: string;
  statusCode?: string;
  description?: string;
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

const BASE_URL = "/api/generic-status-types";

export const genericStatusTypeApi = {
  create: async (
    data: GenericStatusTypeFormData
  ): Promise<ApiResponse<GenericStatusType>> => {
    const response = await api.post<ApiResponse<GenericStatusType>>(
      BASE_URL,
      data
    );
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<PageResponse<GenericStatusType>>> => {
    const response = await api.get<
      ApiResponse<PageResponse<GenericStatusType>>
    >(BASE_URL, {
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
  ): Promise<ApiResponse<PageResponse<GenericStatusType>>> => {
    const response = await api.get<
      ApiResponse<PageResponse<GenericStatusType>>
    >(`${BASE_URL}/search`, {
      params: { searchTerm, page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  getList: async (): Promise<ApiResponse<GenericStatusType[]>> => {
    const response = await api.get<ApiResponse<GenericStatusType[]>>(
      `${BASE_URL}/list`
    );
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<GenericStatusType>> => {
    const response = await api.get<ApiResponse<GenericStatusType>>(
      `${BASE_URL}/${id}`
    );
    return response.data;
  },

  update: async (
    id: number,
    data: GenericStatusTypeFormData
  ): Promise<ApiResponse<GenericStatusType>> => {
    const response = await api.put<ApiResponse<GenericStatusType>>(
      `${BASE_URL}/${id}`,
      data
    );
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },
};
