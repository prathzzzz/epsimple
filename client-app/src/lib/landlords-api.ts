import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { type BackendPageResponse, flattenPageResponse, type FlatPageResponse } from '@/lib/api-utils';

export interface Landlord {
  id: number;
  landlordDetailsId: number;
  landlordName: string;
  landlordEmail: string;
  landlordPhone: string;
  rentSharePercentage?: number;
  createdAt: string;
  updatedAt: string;
}

export interface LandlordFormData {
  landlordDetailsId: number;
  rentSharePercentage?: number;
}

export interface GetAllLandlordsParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
  search?: string;
}

interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
  timestamp: string;
}

const landlordsApi = {
  getAll: async (params: GetAllLandlordsParams = {}): Promise<FlatPageResponse<Landlord>> => {
    const { page = 0, size = 10, sortBy = 'id', sortDirection = 'ASC', search } = params;
    
    // Use search endpoint if search term is provided
    if (search?.trim()) {
      const response = await api.get<ApiResponse<BackendPageResponse<Landlord>>>('/api/landlords/search', {
        params: { searchTerm: search, page, size, sortBy, sortDirection },
      });
      return flattenPageResponse(response.data.data);
    }
    
    const response = await api.get<ApiResponse<BackendPageResponse<Landlord>>>('/api/landlords', {
      params: { page, size, sortBy, sortDirection },
    });
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<Landlord[]> => {
    const response = await api.get<ApiResponse<Landlord[]>>('/api/landlords/list');
    return response.data.data;
  },

  getById: async (id: number): Promise<Landlord> => {
    const response = await api.get<ApiResponse<Landlord>>(`/api/landlords/${id}`);
    return response.data.data;
  },

  create: async (data: LandlordFormData): Promise<Landlord> => {
    const response = await api.post<ApiResponse<Landlord>>('/api/landlords', data);
    return response.data.data;
  },

  update: async (id: number, data: LandlordFormData): Promise<Landlord> => {
    const response = await api.put<ApiResponse<Landlord>>(`/api/landlords/${id}`, data);
    return response.data.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/api/landlords/${id}`);
  },
};

// React Query hooks
export const useLandlords = (params: GetAllLandlordsParams = {}) => {
  return useQuery({
    queryKey: ['landlords', params],
    queryFn: async () => {
      const result = await landlordsApi.getAll(params);
      return result;
    },
  });
};

export const useLandlordsList = () => {
  return useQuery({
    queryKey: ['landlords', 'list'],
    queryFn: () => landlordsApi.getList(),
  });
};

export const useLandlord = (id: number) => {
  return useQuery({
    queryKey: ['landlords', id],
    queryFn: () => landlordsApi.getById(id),
    enabled: !!id,
  });
};

export const useCreateLandlord = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: landlordsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['landlords'] });
    },
  });
};

export const useUpdateLandlord = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: LandlordFormData }) =>
      landlordsApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['landlords'] });
    },
  });
};

export const useDeleteLandlord = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: landlordsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['landlords'] });
    },
  });
};

export const useSearchLandlords = (searchTerm: string) => {
  return useQuery({
    queryKey: ['landlords', 'search', searchTerm],
    queryFn: async () => {
      const endpoint = searchTerm?.trim() ? '/api/landlords/search' : '/api/landlords';
      const params: Record<string, unknown> = {
        page: 0,
        size: 20,
        sortBy: 'id',
        sortDirection: 'ASC',
      };
      
      if (searchTerm?.trim()) {
        params.searchTerm = searchTerm.trim();
      }
      
      const response = await api.get<ApiResponse<BackendPageResponse<Landlord>>>(endpoint, {
        params,
      });
      return flattenPageResponse(response.data.data).content;
    },
    staleTime: 30000,
  });
};
