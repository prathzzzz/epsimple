import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import type { DepreciationCalculation, AssetFinancialSummary } from './schema';

interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

const ASSET_FINANCIALS_ENDPOINTS = {
  CALCULATE_DEPRECIATION: '/api/assets/financials/depreciation',
  ASSET_FINANCIAL_DETAILS: (assetId: number) => `/api/assets/${assetId}/financials`,
  SUMMARY: '/api/assets/financials/summary',
};

export const assetFinancialsApi = {
  useCalculateDepreciation: (params: {
    assetId: number;
    fromDate: string;
    toDate: string;
  }) => {
    return useQuery({
      queryKey: ['asset-depreciation', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<DepreciationCalculation>>(
          ASSET_FINANCIALS_ENDPOINTS.CALCULATE_DEPRECIATION,
          {
            params: {
              assetId: params.assetId,
              fromDate: params.fromDate,
              toDate: params.toDate,
            },
          }
        );
        return response.data.data;
      },
      enabled: !!params.assetId && !!params.fromDate && !!params.toDate,
    });
  },

  useAssetFinancialDetails: (assetId: number) => {
    return useQuery({
      queryKey: ['asset-financial-details', assetId],
      queryFn: async () => {
        const response = await api.get<ApiResponse<DepreciationCalculation>>(
          ASSET_FINANCIALS_ENDPOINTS.ASSET_FINANCIAL_DETAILS(assetId)
        );
        return response.data.data;
      },
      enabled: !!assetId,
    });
  },

  useFinancialSummary: (params?: {
    fromDate?: string;
    toDate?: string;
  }) => {
    return useQuery({
      queryKey: ['asset-financial-summary', params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<AssetFinancialSummary>>(
          ASSET_FINANCIALS_ENDPOINTS.SUMMARY,
          { params }
        );
        return response.data.data;
      },
    });
  },
};
