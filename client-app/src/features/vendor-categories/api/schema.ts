import { z } from 'zod';

export const vendorCategorySchema = z.object({
  id: z.number(),
  categoryName: z.string(),
  description: z.string(),
  createdAt: z.string(),
  updatedAt: z.string(),
});

export const vendorCategoryFormSchema = z.object({
  categoryName: z
    .string()
    .min(1, 'Category name is required')
    .max(100, 'Category name must not exceed 100 characters'),
  description: z
    .string()
    .max(5000, 'Description must not exceed 5000 characters')
    .optional(),
});

export type VendorCategory = z.infer<typeof vendorCategorySchema>;
export type VendorCategoryFormData = z.infer<typeof vendorCategoryFormSchema>;
