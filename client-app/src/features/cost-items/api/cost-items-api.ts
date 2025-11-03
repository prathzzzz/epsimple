import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import api from '@/lib/api';
import { BackendPageResponse, flattenPageResponse } from '@/lib/api-utils';

export interface CostItem {
  id: number;
  costTypeId: number;
  costTypeName: string;
  costCategoryName: string;
  costItemFor: string;
  itemDescription: string | null;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface CostItemFormData {
  costTypeId: number;
  costItemFor: string;
  itemDescription?: string;
}

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

const COST_ITEM_ENDPOINTS = {
  BASE: '/api/cost-items',
  SEARCH: '/api/cost-items/search',
  LIST: '/api/cost-items/list',
  BY_ID: (id: number) => `/api/cost-items/${id}`,
};

export const useCostItems = (params: {
  page: number;
  size: number;
  sortBy?: string;
  sortDirection?: string;
  search?: string;
}) => {
  return useQuery({
    queryKey: ['cost-items', params],
    queryFn: async () => {
      const response = await api.get<ApiResponse<BackendPageResponse<CostItem>>>(
        COST_ITEM_ENDPOINTS.BASE,
        {
          params: {
            page: params.page,
            size: params.size,
            ...(params.sortBy && { sortBy: params.sortBy }),
            ...(params.sortDirection && { sortDirection: params.sortDirection }),
            ...(params.search && { searchTerm: params.search }),
          },
        }
      );
      return flattenPageResponse(response.data.data);
    },
  });
};

export const useCostItemsList = () => {
  return useQuery({
    queryKey: ['cost-items', 'list'],
    queryFn: async () => {
      const response = await api.get<ApiResponse<CostItem[]>>(COST_ITEM_ENDPOINTS.LIST);
      return response.data.data;
    },
  });
};

export const useCostItem = (id: number | null) => {
  return useQuery({
    queryKey: ['cost-items', id],
    queryFn: async () => {
      if (!id) return null;
      const response = await api.get<ApiResponse<CostItem>>(COST_ITEM_ENDPOINTS.BY_ID(id));
      return response.data.data;
    },
    enabled: !!id,
  });
};

export const useCreateCostItem = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (data: CostItemFormData) => {
      const response = await api.post<ApiResponse<CostItem>>(COST_ITEM_ENDPOINTS.BASE, data);
      return response.data.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cost-items'] });
      toast.success('Cost item created successfully');
    },
    onError: (error: any) => {
      const errorMessage = error?.response?.data?.message || 'Failed to create cost item';
      toast.error(errorMessage);
    },
  });
};

export const useUpdateCostItem = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async ({ id, data }: { id: number; data: CostItemFormData }) => {
      const response = await api.put<ApiResponse<CostItem>>(COST_ITEM_ENDPOINTS.BY_ID(id), data);
      return response.data.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cost-items'] });
      toast.success('Cost item updated successfully');
    },
    onError: (error: any) => {
      const errorMessage = error?.response?.data?.message || 'Failed to update cost item';
      toast.error(errorMessage);
    },
  });
};

export const useDeleteCostItem = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (id: number) => {
      await api.delete(COST_ITEM_ENDPOINTS.BY_ID(id));
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cost-items'] });
      toast.success('Cost item deleted successfully');
    },
    onError: (error: any) => {
      const errorMessage = error?.response?.data?.message || 'Failed to delete cost item';
      toast.error(errorMessage);
    },
  });
};
