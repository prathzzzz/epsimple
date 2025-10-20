import { z } from 'zod'

// Zod schema for state validation
export const stateSchema = z.object({
  id: z.number(),
  stateName: z.string(),
  stateCode: z.string(),
  stateCodeAlt: z.string().nullable().optional(),
  createdAt: z.string(),
})

// Schema for form data
export const stateFormSchema = z.object({
  stateName: z
    .string()
    .min(1, 'State name is required')
    .max(100, 'State name cannot exceed 100 characters'),
  stateCode: z
    .string()
    .min(2, 'State code must be at least 2 characters')
    .max(10, 'State code cannot exceed 10 characters')
    .regex(/^[A-Za-z0-9_-]+$/, 'State code can only contain letters, numbers, hyphens and underscores'),
  stateCodeAlt: z
    .string()
    .max(10, 'Alternate state code cannot exceed 10 characters')
    .regex(/^[A-Za-z0-9_-]*$/, 'Alternate state code can only contain letters, numbers, hyphens and underscores')
    .optional()
    .or(z.literal('')),
})

// Type exports
export type State = z.infer<typeof stateSchema>
export type StateFormData = z.infer<typeof stateFormSchema>
