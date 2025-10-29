import { z } from 'zod'

export const activityWorkSchema = z.object({
  activitiesId: z.number().positive('Activity is required'),
  vendorId: z.number().positive('Vendor is required'),
  vendorOrderNumber: z.string().max(100, 'Vendor order number must not exceed 100 characters').optional().or(z.literal('')),
  workOrderDate: z.string().optional().or(z.literal('')),
  workStartDate: z.string().optional().or(z.literal('')),
  workCompletionDate: z.string().optional().or(z.literal('')),
  statusTypeId: z.number().positive('Status type is required'),
})

export type ActivityWorkFormData = z.infer<typeof activityWorkSchema>

export interface ActivityWork {
  id: number
  activitiesId: number
  activitiesName: string
  vendorId: number
  vendorName: string
  vendorOrderNumber: string | null
  workOrderDate: string | null
  workStartDate: string | null
  workCompletionDate: string | null
  statusTypeId: number
  statusTypeName: string
}
