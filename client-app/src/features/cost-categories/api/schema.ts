import { z } from "zod";

export const costCategorySchema = z.object({
  id: z.number(),
  categoryName: z.string(),
  categoryDescription: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const costCategoryFormSchema = z.object({
  categoryName: z
    .string()
    .min(1, "Category name is required")
    .max(50, "Category name must not exceed 50 characters"),
  categoryDescription: z
    .string()
    .max(5000, "Description must not exceed 5000 characters")
    .optional(),
});

export type CostCategory = z.infer<typeof costCategorySchema>;
export type CostCategoryFormData = z.infer<typeof costCategoryFormSchema>;
