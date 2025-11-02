import api from '@/lib/api';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

interface PlacementRecord {
  id: number;
  assetId: number;
}

/**
 * Helper function to remove an asset from its current location
 * @param assetId - The ID of the asset to remove
 * @param locationType - The type of location (site, warehouse, datacenter)
 * @returns Promise that resolves when the asset is removed
 */
export async function removeAssetFromCurrentLocation(
  assetId: number,
  locationType: 'site' | 'warehouse' | 'datacenter'
): Promise<void> {
  let endpoint = '';
  
  if (locationType === 'site') {
    endpoint = '/api/assets-on-site';
  } else if (locationType === 'warehouse') {
    endpoint = '/api/assets-on-warehouse';
  } else if (locationType === 'datacenter') {
    endpoint = '/api/assets-on-datacenter';
  }

  // First, find the placement record by asset ID
  const response = await api.get<ApiResponse<{ content: PlacementRecord[] }>>(endpoint, {
    params: {
      page: 0,
      size: 1,
      sortBy: 'id',
      sortOrder: 'asc',
    },
  });

  // Search through results to find the placement for this asset
  // Note: This is a workaround. Ideally, there should be a backend endpoint to find by assetId
  const placements = response.data.data.content;
  const placement = placements.find((p) => p.assetId === assetId);

  if (placement) {
    // Delete the placement
    await api.delete(`${endpoint}/${placement.id}`);
  }
}
