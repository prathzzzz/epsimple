import { z } from 'zod';

export const locationFormSchema = z.object({
  locationName: z
    .string()
    .min(1, 'Location name is required')
    .max(255, 'Location name cannot exceed 255 characters'),
  address: z
    .string()
    .max(5000, 'Address cannot exceed 5000 characters')
    .optional()
    .or(z.literal('')),
  district: z
    .string()
    .max(100, 'District name cannot exceed 100 characters')
    .optional()
    .or(z.literal('')),
  cityId: z.number().min(1, 'City is required'),
  pincode: z
    .string()
    .regex(/^[0-9]{6}$/, 'Pincode must be exactly 6 digits')
    .optional()
    .or(z.literal('')),
  region: z
    .string()
    .max(50, 'Region cannot exceed 50 characters')
    .optional()
    .or(z.literal('')),
  zone: z
    .string()
    .max(50, 'Zone cannot exceed 50 characters')
    .optional()
    .or(z.literal('')),
  longitude: z
    .number()
    .min(-180, 'Longitude must be between -180 and 180')
    .max(180, 'Longitude must be between -180 and 180')
    .optional()
    .nullable(),
  latitude: z
    .number()
    .min(-90, 'Latitude must be between -90 and 90')
    .max(90, 'Latitude must be between -90 and 90')
    .optional()
    .nullable(),
});

export type LocationFormData = z.infer<typeof locationFormSchema>;

export interface Location {
  id: number;
  locationName: string;
  address?: string;
  district?: string;
  cityId: number;
  cityName: string;
  stateName: string;
  pincode?: string;
  region?: string;
  zone?: string;
  longitude?: number;
  latitude?: number;
  createdAt: string;
  updatedAt: string;
}
