import { z } from 'zod';

export interface AssetFinancialDetails {
  // Asset Info
  assetId: number;
  assetTagId: string;
  assetName: string;
  assetCategoryName: string;
  assetCategoryCode: string;
  assetTypeName: string;
  
  // Financial Info
  revisedCapitalValue: number; // This is purchaseOrderCost
  depreciationPercentage: number; // From asset category
  
  // Site Info (if deployed)
  siteId?: number;
  siteCode?: string;
  siteName?: string;
  techLiveDate?: string; // From site
  
  // Calculated values (from backend)
  deployedOn?: string; // From assets_on_site
  decommissionedOn?: string; // Scrapped date
  
  // Status
  statusTypeName: string;
  isScraped: boolean;
}

export interface DepreciationCalculation {
  assetId: number;
  assetTagId: string;
  assetName: string;
  assetCategoryName: string;
  
  // Values
  revisedCapitalValue: number;
  depreciationPercentage: number;
  
  // Date Range
  fromDate: string;
  toDate: string;
  
  // Calculated
  totalDays: number;
  totalYears: number;
  depreciationAmount: number;
  writtenDownValue: number; // WDV = Revised Capital Value - Depreciation
  
  // If scraped
  isScraped: boolean;
  scrapedDate?: string;
  lossValue?: number; // WDV at time of scrapping
}

export interface AssetFinancialSummary {
  totalAssets: number;
  totalCapitalValue: number;
  totalDepreciation: number;
  totalWDV: number;
  scrapedAssets: number;
  totalLossValue: number;
}

export const depreciationCalculationSchema = z.object({
  assetId: z.number().min(1, 'Asset is required'),
  fromDate: z.string().min(1, 'From date is required'),
  toDate: z.string().min(1, 'To date is required'),
});

export type DepreciationCalculationFormData = z.infer<typeof depreciationCalculationSchema>;
