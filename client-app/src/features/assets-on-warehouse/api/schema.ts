import { z } from 'zod';

export const assetsOnWarehouseFormSchema = z.object({
  assetId: z.number().min(1, 'Asset is required'),
  warehouseId: z.number().min(1, 'Warehouse is required'),
  assetStatusId: z.number().min(1, 'Status is required'),
  activityWorkId: z.number().optional().nullable(),
  assignedOn: z.string().optional().or(z.literal('')),
  deliveredOn: z.string().optional().or(z.literal('')),
  commissionedOn: z.string().optional().or(z.literal('')),
  // vacatedOn removed - now automatically managed by backend
  disposedOn: z.string().optional().or(z.literal('')),
  scrappedOn: z.string().optional().or(z.literal('')),
});

export type AssetsOnWarehouseFormData = z.infer<typeof assetsOnWarehouseFormSchema>;

export interface AssetsOnWarehouse {
  id: number;
  assetId: number;
  assetTagId: string;
  assetName: string;
  assetTypeName: string;
  assetCategoryName: string;
  warehouseId: number;
  warehouseCode: string;
  warehouseName: string;
  assetStatusId: number;
  assetStatusName: string;
  activityWorkId?: number;
  activityWorkNumber?: string;
  assignedOn?: string;
  deliveredOn?: string;
  commissionedOn?: string;
  vacatedOn?: string;
  disposedOn?: string;
  scrappedOn?: string;
  createdAt: string;
  updatedAt: string;
}
