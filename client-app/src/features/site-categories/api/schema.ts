import { z } from "zod";

export const siteCategoryFormSchema = z.object({
  categoryName: z
    .string()
    .min(1, "Category name is required")
    .max(100, "Category name cannot exceed 100 characters"),
  categoryCode: z
    .string()
    .max(20, "Category code cannot exceed 20 characters")
    .regex(
      /^[A-Z0-9_-]+$/,
      "Category code must be uppercase alphanumeric with hyphens/underscores"
    )
    .optional()
    .or(z.literal("")),
  description: z
    .string()
    .max(5000, "Description cannot exceed 5000 characters")
    .optional()
    .or(z.literal("")),
});

export const siteCategoryResponseSchema = z.object({
  id: z.number(),
  categoryName: z.string(),
  categoryCode: z.string().nullable().optional(),
  description: z.string().nullable().optional(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string().nullable().optional(),
  updatedBy: z.string().nullable().optional(),
});

export type SiteCategoryFormData = z.infer<typeof siteCategoryFormSchema>;
export type SiteCategory = z.infer<typeof siteCategoryResponseSchema>;
