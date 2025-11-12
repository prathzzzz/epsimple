import { z } from 'zod';

export const vendorFormSchema = z.object({
  vendorTypeId: z.number({
    message: 'Vendor type is required',
  }).positive('Vendor type is required'),
  vendorDetailsId: z.number({
    message: 'Vendor details is required',
  }).positive('Vendor details is required'),
  vendorCodeAlt: z
    .string()
    .min(1, 'Vendor code is required')
    .max(10, 'Vendor code must not exceed 10 characters')
    .regex(
      /^[A-Z0-9_-]+$/,
      'Vendor code must contain only uppercase letters, numbers, hyphens, and underscores'
    ),
});

export type VendorFormValues = z.infer<typeof vendorFormSchema>;
