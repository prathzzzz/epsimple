import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';

export interface Vendor {
  id: number;
  vendorTypeId: number;
  vendorTypeName: string;
  vendorCategoryName: string;
  vendorDetailsId: number;
  vendorName: string;
  vendorEmail: string;
  vendorContact: string;
  vendorCodeAlt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface VendorFormData {
  vendorTypeId: number;
  vendorDetailsId: number;
  vendorCodeAlt?: string;
}

export interface GetAllVendorsParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
  search?: string;
}

export interface VendorsByTypeParams {
  vendorTypeId: number;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
}

interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  page: {
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
  };
}

// Use the shared FlatPageResponse from api-utils
export type { FlatPageResponse };

const vendorsApi = {
  getAll: async (params: GetAllVendorsParams = {}): Promise<FlatPageResponse<Vendor>> => {
    const { page = 0, size = 10, sortBy = 'id', sortDirection = 'ASC', search } = params;
    
    // Use search endpoint if search term is provided
    if (search && search.trim()) {
      const response = await api.get<ApiResponse<BackendPageResponse<Vendor>>>('/api/vendors/search', {
        params: { searchTerm: search, page, size, sortBy, sortDirection },
      });
      return flattenPageResponse(response.data.data);
    }
    
    const response = await api.get<ApiResponse<BackendPageResponse<Vendor>>>('/api/vendors', {
      params: { page, size, sortBy, sortDirection },
    });
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<Vendor[]> => {
    const response = await api.get<ApiResponse<Vendor[]>>('/api/vendors/list');
    return response.data.data;
  },

  getById: async (id: number): Promise<Vendor> => {
    const response = await api.get<ApiResponse<Vendor>>(`/api/vendors/${id}`);
    return response.data.data;
  },

  getByType: async (params: VendorsByTypeParams): Promise<FlatPageResponse<Vendor>> => {
    const { vendorTypeId, page = 0, size = 10, sortBy = 'id', sortDirection = 'ASC' } = params;
    const response = await api.get<ApiResponse<BackendPageResponse<Vendor>>>(`/api/vendors/type/${vendorTypeId}`, {
      params: { page, size, sortBy, sortDirection },
    });
    return flattenPageResponse(response.data.data);
  },

  create: async (data: VendorFormData): Promise<Vendor> => {
    const response = await api.post<ApiResponse<Vendor>>('/api/vendors', data);
    return response.data.data;
  },

  update: async (id: number, data: VendorFormData): Promise<Vendor> => {
    const response = await api.put<ApiResponse<Vendor>>(`/api/vendors/${id}`, data);
    return response.data.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/api/vendors/${id}`);
  },
};

// React Query hooks
export const useVendors = (params: GetAllVendorsParams = {}) => {
  return useQuery({
    queryKey: ['vendors', params],
    queryFn: async () => {
      const result = await vendorsApi.getAll(params);
      return result;
    },
  });
};

export const useVendorsList = () => {
  return useQuery({
    queryKey: ['vendors', 'list'],
    queryFn: () => vendorsApi.getList(),
  });
};

export const useVendor = (id: number) => {
  return useQuery({
    queryKey: ['vendors', id],
    queryFn: () => vendorsApi.getById(id),
    enabled: !!id,
  });
};

export const useVendorsByType = (params: VendorsByTypeParams) => {
  return useQuery({
    queryKey: ['vendors', 'type', params],
    queryFn: () => vendorsApi.getByType(params),
    enabled: !!params.vendorTypeId,
  });
};

export const useCreateVendor = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: vendorsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vendors'] });
    },
  });
};

export const useUpdateVendor = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: VendorFormData }) =>
      vendorsApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vendors'] });
    },
  });
};

export const useDeleteVendor = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: vendorsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vendors'] });
    },
  });
};

export const useSearchVendors = (searchTerm: string) => {
  return useQuery({
    queryKey: ['vendors', 'search', searchTerm],
    queryFn: async () => {
      const endpoint = searchTerm?.trim() ? '/api/vendors/search' : '/api/vendors';
      const params: Record<string, unknown> = {
        page: 0,
        size: 20,
        sortBy: 'id',
        sortDirection: 'ASC',
      };
      
      if (searchTerm?.trim()) {
        params.searchTerm = searchTerm.trim();
      }
      
      const response = await api.get<ApiResponse<BackendPageResponse<Vendor>>>(endpoint, {
        params,
      });
      return flattenPageResponse(response.data.data).content;
    },
    staleTime: 30000,
  });
};
