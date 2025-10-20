import { z } from 'zod'

export const bankSchema = z.object({
  id: z.number(),
  bankName: z.string(),
  rbiBankCode: z.string().nullable(),
  epsBankCode: z.string().nullable(),
  bankCodeAlt: z.string().nullable(),
  bankLogo: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
})

export type Bank = z.infer<typeof bankSchema>

export const bankFormSchema = z.object({
  bankName: z.string().min(1, 'Bank name is required').max(255, 'Bank name cannot exceed 255 characters'),
  rbiBankCode: z.string().max(10, 'RBI bank code cannot exceed 10 characters').regex(/^[A-Za-z0-9]*$/, 'RBI bank code can only contain letters and numbers').optional().or(z.literal('')),
  epsBankCode: z.string().max(10, 'EPS bank code cannot exceed 10 characters').regex(/^[A-Za-z0-9_-]*$/, 'EPS bank code can only contain letters, numbers, hyphens and underscores').optional().or(z.literal('')),
  bankCodeAlt: z.string().max(10, 'Alternate bank code cannot exceed 10 characters').regex(/^[A-Za-z0-9_-]*$/, 'Alternate bank code can only contain letters, numbers, hyphens and underscores').optional().or(z.literal('')),
  bankLogo: z.string().max(500, 'Bank logo URL cannot exceed 500 characters').optional().or(z.literal('')),
})

export type BankFormData = z.infer<typeof bankFormSchema>
