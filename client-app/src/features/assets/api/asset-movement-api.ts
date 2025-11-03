import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface AssetMovementHistory {
  id: number;
  assetId: number;
  assetTagId: string;
  assetName: string;
  movementTypeId: number;
  movementType: string;
  movementDescription: string;
  movedAt: string;
  createdAt: string;
  
  // From location details
  fromFactory?: string;
  fromSiteId?: number;
  fromSiteCode?: string;
  fromSiteName?: string;
  fromWarehouseId?: number;
  fromWarehouseCode?: string;
  fromWarehouseName?: string;
  fromDatacenterId?: number;
  fromDatacenterCode?: string;
  fromDatacenterName?: string;
  
  // To location details
  toSiteId?: number;
  toSiteCode?: string;
  toSiteName?: string;
  toWarehouseId?: number;
  toWarehouseCode?: string;
  toWarehouseName?: string;
  toDatacenterId?: number;
  toDatacenterCode?: string;
  toDatacenterName?: string;
}

export interface AssetCurrentLocation {
  assetId: number;
  assetTagId: string;
  assetName: string;
  isPlaced: boolean;
  locationType?: 'site' | 'warehouse' | 'datacenter';
  placementId?: number;
  locationId?: number;
  locationCode?: string;
  locationName?: string;
  assignedOn?: string;
  deliveredOn?: string;
  assetStatusName?: string;
  activityWorkId?: number;
  activityWorkNumber?: string;
}

export const assetMovementApi = {
  /**
   * Get movement history for an asset with pagination
   */
  useMovementHistory: (assetId: number, page: number = 0, size: number = 20) => {
    return useQuery({
      queryKey: ['asset-movement-history', assetId, page, size],
      queryFn: async () => {
        const response = await api.get<ApiResponse<PageResponse<AssetMovementHistory>>>(
          `/api/assets/${assetId}/movement-history`,
          {
            params: { page, size }
          }
        );
        return response.data.data;
      },
      enabled: assetId > 0,
    });
  },

  /**
   * Get current location of an asset
   */
  useCurrentLocation: (assetId: number) => {
    return useQuery({
      queryKey: ['asset-current-location', assetId],
      queryFn: async () => {
        const response = await api.get<ApiResponse<AssetCurrentLocation>>(
          `/api/assets/${assetId}/current-location`
        );
        return response.data.data;
      },
      enabled: assetId > 0,
    });
  },

  /**
   * Get movement history (non-hook version for direct calls)
   */
  getMovementHistory: async (assetId: number, page: number = 0, size: number = 20): Promise<PageResponse<AssetMovementHistory>> => {
    const response = await api.get<ApiResponse<PageResponse<AssetMovementHistory>>>(
      `/api/assets/${assetId}/movement-history`,
      {
        params: { page, size }
      }
    );
    return response.data.data;
  },

  /**
   * Get current location (non-hook version for direct calls)
   */
  getCurrentLocation: async (assetId: number): Promise<AssetCurrentLocation> => {
    const response = await api.get<ApiResponse<AssetCurrentLocation>>(
      `/api/assets/${assetId}/current-location`
    );
    return response.data.data;
  },
};
