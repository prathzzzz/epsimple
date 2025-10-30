import { z } from 'zod'

// Zod Schema
export const activityWorkRemarksSchema = z.object({
  activityWorkId: z.number().min(1, 'Activity work is required'),
  comment: z.string().min(1, 'Comment is required').max(5000, 'Comment too long'),
  commentedBy: z.number().optional(),
})

// Types
export interface ActivityWorkRemark {
  id: number
  activityWorkId: number
  comment: string
  commentedOn: string
  commentedBy?: number
  commentedByName?: string
}

export type ActivityWorkRemarkRequest = z.infer<typeof activityWorkRemarksSchema>
