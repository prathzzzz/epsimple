import { z } from 'zod';

// VendorCategory nested object in response
export const vendorCategoryResponseSchema = z.object({
  id: z.number(),
  categoryName: z.string(),
  description: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
});

export const vendorTypeSchema = z.object({
  id: z.number(),
  typeName: z.string(),
  vendorCategory: vendorCategoryResponseSchema,
  description: z.string(),
  createdAt: z.string(),
  updatedAt: z.string(),
});

export const vendorTypeFormSchema = z.object({
  typeName: z
    .string()
    .min(1, 'Vendor type name is required')
    .max(100, 'Vendor type name must not exceed 100 characters'),
  vendorCategoryId: z
    .number({ message: 'Vendor category is required' })
    .min(1, 'Vendor category is required'),
  description: z
    .string()
    .max(5000, 'Description must not exceed 5000 characters')
    .optional(),
});

export type VendorType = z.infer<typeof vendorTypeSchema>;
export type VendorTypeFormData = z.infer<typeof vendorTypeFormSchema>;
