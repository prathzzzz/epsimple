import { z } from 'zod';

export const costItemSchema = z.object({
  costTypeId: z.number({
    message: 'Cost type is required',
  }).positive('Cost type is required'),
  costItemFor: z.string()
    .min(1, 'Cost item for is required')
    .max(255, 'Cost item for must not exceed 255 characters'),
  itemDescription: z.string()
    .max(1000, 'Description must not exceed 1000 characters')
    .optional(),
});

export type CostItemFormValues = z.infer<typeof costItemSchema>;
