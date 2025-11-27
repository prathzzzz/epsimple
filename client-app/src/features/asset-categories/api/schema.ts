import { z } from "zod";

export interface AssetCategory {
  id: number;
  categoryName: string;
  categoryCode: string;
  assetCodeAlt: string;
  description: string | null;
  depreciation: number | null;
  createdAt: string;
  updatedAt: string;
}

export const assetCategoryFormSchema = z.object({
  categoryName: z
    .string()
    .min(1, "Category name is required")
    .max(100, "Category name cannot exceed 100 characters"),
  categoryCode: z
    .string()
    .min(1, "Category code is required")
    .max(20, "Category code cannot exceed 20 characters")
    .regex(/^[A-Z0-9_&-]+$/, "Category code must be uppercase with no spaces"),
  assetCodeAlt: z
    .string()
    .min(1, "Asset code alt is required")
    .max(10, "Asset code alt cannot exceed 10 characters")
    .regex(/^[A-Z0-9]+$/, "Asset code alt must be uppercase with no spaces"),
  description: z
    .string()
    .max(5000, "Description cannot exceed 5000 characters")
    .optional()
    .or(z.literal("")),
  depreciation: z
    .number()
    .min(0, "Depreciation cannot be negative")
    .max(100, "Depreciation cannot exceed 100%")
    .nullable()
    .optional(),
});

export type AssetCategoryFormData = z.infer<typeof assetCategoryFormSchema>;

