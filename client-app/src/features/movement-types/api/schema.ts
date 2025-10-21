import { z } from 'zod';

// MovementType schema for API responses
export const movementTypeSchema = z.object({
  id: z.number(),
  movementType: z.string(),
  description: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string().nullable(),
  updatedBy: z.string().nullable(),
});

// MovementType form schema for create/update
export const movementTypeFormSchema = z.object({
  movementType: z.string().min(1, 'Movement type is required').max(100, 'Movement type must not exceed 100 characters'),
  description: z.string().max(5000, 'Description must not exceed 5000 characters').optional().or(z.literal('')),
});

export type MovementType = z.infer<typeof movementTypeSchema>;
export type MovementTypeFormData = z.infer<typeof movementTypeFormSchema>;
