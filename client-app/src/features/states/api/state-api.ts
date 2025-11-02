import api from "@/lib/api";
import { useQuery } from '@tanstack/react-query';

const STATE_ENDPOINTS = {
  LIST: "/api/states/list",
};

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

export interface State {
  id: number;
  stateName: string;
  stateCode: string;
}

export const stateApi = {
  getList: async (): Promise<State[]> => {
    const response = await api.get<ApiResponse<State[]>>(STATE_ENDPOINTS.LIST);
    return response.data.data;
  },

  useSearch: (searchTerm: string) => {
    return useQuery({
      queryKey: ['states', 'search', searchTerm],
      queryFn: async () => {
        const response = await api.get<ApiResponse<State[]>>(STATE_ENDPOINTS.LIST);
        const allStates = response.data.data;
        
        // If no search term, return first 20 states
        if (!searchTerm || searchTerm.trim().length === 0) {
          return allStates.slice(0, 20);
        }
        
        // Client-side filtering since there's no search endpoint
        return allStates.filter(state => 
          state.stateName.toLowerCase().includes(searchTerm.toLowerCase()) ||
          state.stateCode?.toLowerCase().includes(searchTerm.toLowerCase())
        );
      },
      staleTime: 30000,
    });
  },
};
