import { z } from 'zod'

// Zod validation schema
export const assetSchema = z.object({
  assetTagId: z.string().min(1, 'Asset Tag ID is required'),
  assetName: z.string().min(1, 'Asset Name is required'),
  serialNumber: z.string().optional(),
  modelNumber: z.string().optional(),
  assetCategoryId: z.number().min(1, 'Asset Category is required'),
  assetTypeId: z.number().min(1, 'Asset Type is required'),
  vendorId: z.number().min(1, 'Vendor is required'),
  lenderBankId: z.number().min(1, 'Lender Bank is required'),
  statusTypeId: z.number().min(1, 'Status Type is required'),
  ownershipStatusId: z.number().optional(),
  purchaseOrderNumber: z.string().optional(),
  purchaseOrderDate: z.string().optional(),
  purchaseOrderCost: z.number().optional(),
  dispatchOrderNumber: z.string().optional(),
  dispatchOrderDate: z.string().optional(),
  warrantyPeriod: z.number().optional(),
  warrantyExpiryDate: z.string().optional(),
  endOfLifeDate: z.string().optional(),
  endOfSupportDate: z.string().optional(),
})

export type AssetFormData = z.infer<typeof assetSchema>

// API Response Types
export interface Asset {
  id: number
  assetTagId: string
  assetName: string
  serialNumber?: string
  modelNumber?: string
  assetCategoryId: number
  assetCategoryName: string
  assetCategoryCode: string
  assetTypeId: number
  assetTypeName: string
  assetTypeCode: string
  vendorId: number
  vendorName: string
  vendorCode: string
  lenderBankId: number
  lenderBankName: string
  lenderBankCode: string
  statusTypeId: number
  statusTypeName: string
  ownershipStatusId?: number
  ownershipStatusName?: string
  purchaseOrderNumber?: string
  purchaseOrderDate?: string
  purchaseOrderCost?: number
  dispatchOrderNumber?: string
  dispatchOrderDate?: string
  warrantyPeriod?: number
  warrantyExpiryDate?: string
  endOfLifeDate?: string
  endOfSupportDate?: string
  createdAt: string
  updatedAt: string
  createdBy: string
  updatedBy: string
}

export interface AssetRequest {
  assetTagId: string
  assetName: string
  serialNumber?: string
  modelNumber?: string
  assetCategoryId: number
  assetTypeId: number
  vendorId: number
  lenderBankId: number
  statusTypeId: number
  ownershipStatusId?: number
  purchaseOrderNumber?: string
  purchaseOrderDate?: string
  purchaseOrderCost?: number
  dispatchOrderNumber?: string
  dispatchOrderDate?: string
  warrantyPeriod?: number
  warrantyExpiryDate?: string
  endOfLifeDate?: string
  endOfSupportDate?: string
}

export interface AssetListParams {
  page?: number
  size?: number
  search?: string
  sortBy?: string
  sortDir?: 'asc' | 'desc'
}

export interface AssetListResponse {
  content: Asset[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}
