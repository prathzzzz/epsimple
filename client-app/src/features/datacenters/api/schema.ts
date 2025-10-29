import { z } from 'zod'

export const datacenterSchema = z.object({
  datacenterName: z.string().min(1, 'Datacenter name is required').max(100, 'Datacenter name must not exceed 100 characters'),
  datacenterCode: z.string().max(20, 'Datacenter code must not exceed 20 characters').optional().or(z.literal('')),
  datacenterType: z.string().max(50, 'Datacenter type must not exceed 50 characters').optional().or(z.literal('')),
  locationId: z.number().positive('Location is required'),
})

export type DatacenterFormValues = z.infer<typeof datacenterSchema>
export type DatacenterFormData = DatacenterFormValues

export interface Datacenter {
  id: number
  datacenterName: string
  datacenterCode: string | null
  datacenterType: string | null
  locationId: number
  locationName: string
  cityName: string
  stateName: string
}

