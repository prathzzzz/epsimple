import { z } from 'zod'

export const personTypeSchema = z.object({
  id: z.number(),
  typeName: z.string(),
  description: z.string().optional(),
  createdAt: z.string(),
  updatedAt: z.string(),
})

export const personTypeFormSchema = z.object({
  typeName: z
    .string()
    .min(1, 'Person type name is required')
    .max(100, 'Person type name must not exceed 100 characters'),
  description: z
    .string()
    .max(5000, 'Description must not exceed 5000 characters')
    .optional()
    .or(z.literal('')),
})

export type PersonType = z.infer<typeof personTypeSchema>
export type PersonTypeFormValues = z.infer<typeof personTypeFormSchema>
