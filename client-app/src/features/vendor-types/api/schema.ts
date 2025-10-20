import { z } from 'zod';

export const vendorTypeSchema = z.object({
  id: z.number(),
  typeName: z.string(),
  vendorCategory: z.string(),
  description: z.string(),
  createdAt: z.string(),
  updatedAt: z.string(),
});

export const vendorTypeFormSchema = z.object({
  typeName: z
    .string()
    .min(1, 'Vendor type name is required')
    .max(100, 'Vendor type name must not exceed 100 characters'),
  vendorCategory: z
    .string()
    .max(100, 'Vendor category must not exceed 100 characters')
    .optional(),
  description: z
    .string()
    .max(5000, 'Description must not exceed 5000 characters')
    .optional(),
});

export type VendorType = z.infer<typeof vendorTypeSchema>;
export type VendorTypeFormData = z.infer<typeof vendorTypeFormSchema>;
