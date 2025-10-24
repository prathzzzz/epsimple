import axios from 'axios';

const BASE_URL = '/api/vendor-categories';

export interface VendorCategory {
  id: number;
  categoryName: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface VendorCategoryFormData {
  categoryName: string;
  description?: string;
}

export interface VendorCategoriesResponse {
  content: VendorCategory[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}

export const vendorCategoriesApi = {
  getAll: async (
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    sortDirection: string = 'ASC'
  ): Promise<ApiResponse<VendorCategoriesResponse>> => {
    const response = await axios.get(BASE_URL, {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  search: async (
    query: string,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    sortDirection: string = 'ASC'
  ): Promise<ApiResponse<VendorCategoriesResponse>> => {
    const response = await axios.get(`${BASE_URL}/search`, {
      params: { query, page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  getList: async (): Promise<ApiResponse<VendorCategory[]>> => {
    const response = await axios.get(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<VendorCategory>> => {
    const response = await axios.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  create: async (data: VendorCategoryFormData): Promise<ApiResponse<VendorCategory>> => {
    const response = await axios.post(BASE_URL, data);
    return response.data;
  },

  update: async (id: number, data: VendorCategoryFormData): Promise<ApiResponse<VendorCategory>> => {
    const response = await axios.put(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await axios.delete(`${BASE_URL}/${id}`);
    return response.data;
  },
};
