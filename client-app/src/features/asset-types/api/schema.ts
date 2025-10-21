import { z } from 'zod';

export const assetTypeSchema = z.object({
  id: z.number(),
  typeName: z.string(),
  typeCode: z.string(),
  description: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
});

export const assetTypeFormSchema = z.object({
  typeName: z
    .string()
    .min(1, 'Type name is required')
    .max(100, 'Type name must not exceed 100 characters'),
  typeCode: z
    .string()
    .min(1, 'Type code is required')
    .max(20, 'Type code must not exceed 20 characters'),
  description: z
    .string()
    .max(5000, 'Description must not exceed 5000 characters')
    .optional(),
});

export type AssetType = z.infer<typeof assetTypeSchema>;
export type AssetTypeFormData = z.infer<typeof assetTypeFormSchema>;
