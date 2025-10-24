import axios from 'axios';

const BASE_URL = '/api/vendor-types';

export interface VendorType {
  id: number;
  typeName: string;
  vendorCategory: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface VendorTypeFormData {
  typeName: string;
  vendorCategory?: string;
  description?: string;
}

export interface VendorTypesResponse {
  content: VendorType[];
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

export const vendorTypesApi = {
  getAll: async (
    page: number = 0,
    size: number = 10,
    sortBy: string = 'id',
    sortDirection: string = 'ASC'
  ): Promise<ApiResponse<VendorTypesResponse>> => {
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
  ): Promise<ApiResponse<VendorTypesResponse>> => {
    const response = await axios.get(`${BASE_URL}/search`, {
      params: { query, page, size, sortBy, sortDirection },
    });
    return response.data;
  },

  getList: async (): Promise<ApiResponse<VendorType[]>> => {
    const response = await axios.get(`${BASE_URL}/list`);
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<VendorType>> => {
    const response = await axios.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  create: async (data: VendorTypeFormData): Promise<ApiResponse<VendorType>> => {
    const response = await axios.post(BASE_URL, data);
    return response.data;
  },

  update: async (id: number, data: VendorTypeFormData): Promise<ApiResponse<VendorType>> => {
    const response = await axios.put(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await axios.delete(`${BASE_URL}/${id}`);
    return response.data;
  },
};
