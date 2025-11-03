import { z } from 'zod';

export const assetsOnSiteFormSchema = z.object({
  assetId: z.number().min(1, 'Asset is required'),
  siteId: z.number().min(1, 'Site is required'),
  assetStatusId: z.number().min(1, 'Status is required'),
  activityWorkId: z.number().optional().nullable(),
  assignedOn: z.string().optional().or(z.literal('')),
  deliveredOn: z.string().optional().or(z.literal('')),
  deployedOn: z.string().optional().or(z.literal('')),
  activatedOn: z.string().optional().or(z.literal('')),
  decommissionedOn: z.string().optional().or(z.literal('')),
  // vacatedOn removed - now automatically managed by backend
});

export type AssetsOnSiteFormData = z.infer<typeof assetsOnSiteFormSchema>;

export interface AssetsOnSite {
  id: number;
  assetId: number;
  assetTagId: string;
  assetName: string;
  assetTypeName: string;
  assetCategoryName: string;
  siteId: number;
  siteCode: string;
  siteName: string;
  assetStatusId: number;
  assetStatusName: string;
  activityWorkId?: number;
  activityWorkNumber?: string;
  assignedOn?: string;
  deliveredOn?: string;
  deployedOn?: string;
  activatedOn?: string;
  decommissionedOn?: string;
  vacatedOn?: string;
  createdAt: string;
  updatedAt: string;
}
