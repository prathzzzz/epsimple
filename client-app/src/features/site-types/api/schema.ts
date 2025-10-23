import { z } from "zod";

export const siteTypeFormSchema = z.object({
  typeName: z
    .string()
    .min(1, "Site type name is required")
    .max(100, "Type name cannot exceed 100 characters"),
  description: z
    .string()
    .max(5000, "Description cannot exceed 5000 characters")
    .optional()
    .or(z.literal("")),
});

export const siteTypeResponseSchema = z.object({
  id: z.number(),
  typeName: z.string(),
  description: z.string().nullable().optional(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string().nullable().optional(),
  updatedBy: z.string().nullable().optional(),
});

export type SiteTypeFormData = z.infer<typeof siteTypeFormSchema>;
export type SiteType = z.infer<typeof siteTypeResponseSchema>;
