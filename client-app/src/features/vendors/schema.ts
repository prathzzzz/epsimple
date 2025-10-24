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
    .optional()
    .refine(
      (val) => {
        if (!val || val.trim() === '') return true;
        return /^[A-Z0-9]{1,10}$/.test(val);
      },
      {
        message: 'Vendor code must be 1-10 uppercase alphanumeric characters',
      }
    ),
});

export type VendorFormValues = z.infer<typeof vendorFormSchema>;
