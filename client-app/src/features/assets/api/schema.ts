import { z } from 'zod'

// Zod validation schema
export const assetSchema = z.object({
  assetTagId: z.string().min(1, 'Asset Tag ID is required'),
  assetName: z.string().min(1, 'Asset Name is required'),
  serialNumber: z.string().optional(),
  assetCategoryId: z.number().min(1, 'Asset Category is required'),
  assetTypeId: z.number().min(1, 'Asset Type is required'),
  vendorId: z.number().min(1, 'Vendor is required'),
  lenderBankId: z.number().min(1, 'Bank is required'),
  statusTypeId: z.number().min(1, 'Status Type is required'),
  purchaseDate: z.string().optional(),
  purchasePrice: z.number().optional(),
  warrantyExpiryDate: z.string().optional(),
  remarks: z.string().optional(),
})

export type AssetFormData = z.infer<typeof assetSchema>

// API Response Types
export interface Asset {
  id: number
  assetTagId: string
  assetName: string
  serialNumber?: string
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
  purchaseDate?: string
  purchasePrice?: number
  warrantyExpiryDate?: string
  remarks?: string
  createdAt: string
  updatedAt: string
  createdBy: string
  updatedBy: string
}

export interface AssetRequest {
  assetTagId: string
  assetName: string
  serialNumber?: string
  assetCategoryId: number
  assetTypeId: number
  vendorId: number
  lenderBankId: number
  statusTypeId: number
  purchaseDate?: string
  purchasePrice?: number
  warrantyExpiryDate?: string
  remarks?: string
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
