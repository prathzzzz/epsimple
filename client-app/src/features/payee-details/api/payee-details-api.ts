import api from '@/lib/api';
import { BackendPageResponse, FlatPageResponse, flattenPageResponse } from '@/lib/api-utils';
import {
  useMutation,
  useQuery,
  useQueryClient,
  type UseQueryResult,
} from '@tanstack/react-query';
import type { PayeeDetails, PayeeDetailsFormData } from './schema';

// API Response wrapper interface
interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

interface GetAllParams {
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
  search?: string;
}

interface SearchParams extends GetAllParams {
  searchTerm: string;
}

const PAYEE_DETAILS_BASE_URL = '/api/payee-details';

// API methods
export const payeeDetailsApi = {
  getAll: async (params: GetAllParams = {}) => {
    const {
      page = 0,
      size = 10,
      sortBy = 'id',
      sortDirection = 'ASC',
      search,
    } = params;
    
    // If search is provided, use the search endpoint
    if (search && search.trim()) {
      const response = await api.get<ApiResponse<BackendPageResponse<PayeeDetails>>>(
        `${PAYEE_DETAILS_BASE_URL}/search`,
        {
          params: { searchTerm: search, page, size, sortBy, sortDirection },
        }
      );
      return flattenPageResponse(response.data.data);
    }
    
    // Otherwise use the regular getAll endpoint
    const response = await api.get<ApiResponse<BackendPageResponse<PayeeDetails>>>(
      PAYEE_DETAILS_BASE_URL,
      {
        params: { page, size, sortBy, sortDirection },
      }
    );
    return flattenPageResponse(response.data.data);
  },

  search: async (params: SearchParams) => {
    const {
      searchTerm,
      page = 0,
      size = 10,
      sortBy = 'payeeName',
      sortDirection = 'ASC',
    } = params;
    const response = await api.get<ApiResponse<BackendPageResponse<PayeeDetails>>>(
      `${PAYEE_DETAILS_BASE_URL}/search`,
      {
        params: { searchTerm, page, size, sortBy, sortDirection },
      }
    );
    return flattenPageResponse(response.data.data);
  },

  getList: async (): Promise<PayeeDetails[]> => {
    const response = await api.get<ApiResponse<PayeeDetails[]>>(
      `${PAYEE_DETAILS_BASE_URL}/list`
    );
    return response.data.data;
  },

  getById: async (id: number): Promise<PayeeDetails> => {
    const response = await api.get<ApiResponse<PayeeDetails>>(
      `${PAYEE_DETAILS_BASE_URL}/${id}`
    );
    return response.data.data;
  },

  create: async (data: PayeeDetailsFormData): Promise<PayeeDetails> => {
    const response = await api.post<ApiResponse<PayeeDetails>>(
      PAYEE_DETAILS_BASE_URL,
      data
    );
    return response.data.data;
  },

  update: async (
    id: number,
    data: PayeeDetailsFormData
  ): Promise<PayeeDetails> => {
    const response = await api.put<ApiResponse<PayeeDetails>>(
      `${PAYEE_DETAILS_BASE_URL}/${id}`,
      data
    );
    return response.data.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`${PAYEE_DETAILS_BASE_URL}/${id}`);
  },
};

// React Query hooks
export const useGetAllPayeeDetails = (params: GetAllParams = {}) => {
  return useQuery({
    queryKey: ['payeeDetails', params],
    queryFn: () => payeeDetailsApi.getAll(params),
  });
};

export const useSearchPayeeDetails = (params: SearchParams) => {
  return useQuery({
    queryKey: ['payeeDetails', 'search', params],
    queryFn: () => payeeDetailsApi.search(params),
    enabled: !!params.searchTerm,
  });
};

export const useGetPayeeDetailsList = (): UseQueryResult<
  PayeeDetails[],
  Error
> => {
  return useQuery({
    queryKey: ['payeeDetails', 'list'],
    queryFn: payeeDetailsApi.getList,
  });
};

export const useGetPayeeDetailsById = (id: number) => {
  return useQuery({
    queryKey: ['payeeDetails', id],
    queryFn: () => payeeDetailsApi.getById(id),
    enabled: !!id,
  });
};

export const useCreatePayeeDetails = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: payeeDetailsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['payeeDetails'] });
    },
  });
};

export const useUpdatePayeeDetails = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: PayeeDetailsFormData }) =>
      payeeDetailsApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['payeeDetails'] });
    },
  });
};

export const useDeletePayeeDetails = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: payeeDetailsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['payeeDetails'] });
    },
  });
};
