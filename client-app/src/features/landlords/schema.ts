import { z } from 'zod';

export const landlordFormSchema = z.object({
  landlordDetailsId: z.number({
    message: 'Landlord details is required',
  }).positive('Landlord details is required'),
  rentSharePercentage: z
    .number({
      message: 'Rent share percentage must be a number',
    })
    .min(0, 'Rent share percentage must be between 0 and 100')
    .max(100, 'Rent share percentage must be between 0 and 100')
    .optional(),
});

export type LandlordFormValues = z.infer<typeof landlordFormSchema>;
