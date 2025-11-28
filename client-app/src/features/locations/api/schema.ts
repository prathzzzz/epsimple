import { z } from 'zod';

const capitalizeWords = (str: string): string => {
  return str
    .split(/\s+/)
    .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(' ');
};

export const locationFormSchema = z.object({
  locationName: z
    .string()
    .min(1, 'Location name is required')
    .max(255, 'Location name cannot exceed 255 characters')
    .regex(/.*[a-zA-Z].*/, 'Location name must contain at least one alphabetic character')
    .transform(val => capitalizeWords(val.trim())),
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
    .max(10, 'Pincode cannot exceed 10 characters')
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
    .string()
    .max(50, 'Longitude cannot exceed 50 characters')
    .optional()
    .or(z.literal('')),
  latitude: z
    .string()
    .max(50, 'Latitude cannot exceed 50 characters')
    .optional()
    .or(z.literal('')),
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
  longitude?: string;
  latitude?: string;
  createdAt: string;
  updatedAt: string;
}
