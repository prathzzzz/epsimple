import { z } from 'zod';

export const warehouseFormSchema = z.object({
  warehouseName: z
    .string()
    .min(1, 'Warehouse name is required')
    .max(255, 'Warehouse name cannot exceed 255 characters'),
  warehouseCode: z
    .string()
    .max(50, 'Warehouse code cannot exceed 50 characters')
    .regex(/^[A-Z0-9_-]*$/, 'Warehouse code must contain only uppercase letters, numbers, hyphens, and underscores')
    .optional()
    .or(z.literal('')),
  warehouseType: z
    .string()
    .max(100, 'Warehouse type cannot exceed 100 characters')
    .optional()
    .or(z.literal('')),
  locationId: z.number().min(1, 'Location is required'),
});

export type WarehouseFormData = z.infer<typeof warehouseFormSchema>;

export interface Warehouse {
  id: number;
  warehouseName: string;
  warehouseCode?: string;
  warehouseType?: string;
  locationId: number;
  locationName: string;
  cityName: string;
  stateName: string;
  createdAt: string;
  updatedAt: string;
}
