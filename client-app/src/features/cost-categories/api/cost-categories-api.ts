import api from "@/lib/api";
import { useQuery } from "@tanstack/react-query";
import { BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';

export interface CostCategory {
  id: number;
  categoryName: string;
  categoryDescription: string | null;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface CostCategoryFormData {
  categoryName: string;
  categoryDescription?: string;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

const BASE_URL = "/api/cost-categories";

export const costCategoriesApi = {
  create: async (data: CostCategoryFormData): Promise<ApiResponse<CostCategory>> => {
    const response = await api.post<ApiResponse<CostCategory>>(BASE_URL, data);
    return response.data;
  },

  getAll: async (
    page = 0,
    size = 10,
    sortBy = "id",
    sortDirection: "ASC" | "DESC" = "ASC"
  ): Promise<ApiResponse<BackendPageResponse<CostCategory>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<CostCategory>>>(BASE_URL, {
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
  ): Promise<ApiResponse<BackendPageResponse<CostCategory>>> => {
    const response = await api.get<ApiResponse<BackendPageResponse<CostCategory>>>(
      `${BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return response.data;
  },

  getList: async (): Promise<ApiResponse<CostCategory[]>> => {
    const response = await api.get<ApiResponse<CostCategory[]>>(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<CostCategory>> => {
    const response = await api.get<ApiResponse<CostCategory>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  update: async (id: number, data: CostCategoryFormData): Promise<ApiResponse<CostCategory>> => {
    const response = await api.put<ApiResponse<CostCategory>>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.delete<ApiResponse<void>>(`${BASE_URL}/${id}`);
    return response.data;
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['cost-categories', 'search', searchTerm],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<CostCategory>>>(
          searchTerm?.trim() ? `${BASE_URL}/search` : BASE_URL,
          {
            params: {
              ...(searchTerm?.trim() && { searchTerm: searchTerm.trim() }),
              page: 0,
              size: 20,
              sortBy: 'categoryName',
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

export const getAllCostCategoriesList = async (): Promise<ApiResponse<CostCategory[]>> => {
  return costCategoriesApi.getList();
};
