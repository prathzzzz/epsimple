import api from "@/lib/api";

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
};
