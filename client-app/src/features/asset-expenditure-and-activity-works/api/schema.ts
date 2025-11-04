import { z } from 'zod';

export const assetExpenditureAndActivityWorkFormSchema = z.object({
  assetId: z.number().int().positive('Asset is required'),
  expendituresInvoiceId: z.number().int().positive().nullable().optional(),
  activityWorkId: z.number().int().positive().nullable().optional(),
});

export type AssetExpenditureAndActivityWorkFormData = z.infer<typeof assetExpenditureAndActivityWorkFormSchema>;

export interface AssetExpenditureAndActivityWork {
  id: number;
  assetId: number;
  assetTagId: string;
  assetName: string;
  activityWorkId?: number;
  vendorOrderNumber?: string;
  activityName?: string;
  expendituresInvoiceId?: number;
  invoiceNumber?: string;
  amount?: number;
  costItemName?: string;
  createdAt: string;
  updatedAt: string;
}
