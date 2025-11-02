import api from '@/lib/api';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

export interface AssetLocationCheck {
  isPlaced: boolean;
  locationType?: 'site' | 'warehouse' | 'datacenter';
  locationId?: number;
  locationName?: string;
  locationCode?: string;
  assetTagId: string;
}

export const assetLocationApi = {
  checkLocation: async (assetId: number): Promise<AssetLocationCheck> => {
    const response = await api.get<ApiResponse<AssetLocationCheck>>(
      `/api/asset-location/check/${assetId}`
    );
    return response.data.data;
  },

  removeFromCurrentLocation: async (assetId: number): Promise<void> => {
    await api.delete(`/api/asset-location/remove/${assetId}`);
  },
};
