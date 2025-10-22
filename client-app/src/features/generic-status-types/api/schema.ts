import { z } from "zod";

export const genericStatusTypeSchema = z.object({
  id: z.number(),
  statusName: z.string(),
  statusCode: z.string().nullable(),
  description: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const genericStatusTypeFormSchema = z.object({
  statusName: z
    .string()
    .min(1, "Status name is required")
    .max(100, "Status name must not exceed 100 characters"),
  statusCode: z
    .string()
    .max(20, "Status code must not exceed 20 characters")
    .regex(
      /^[A-Z0-9_-]*$/,
      "Status code must be uppercase alphanumeric with hyphens/underscores"
    )
    .optional()
    .or(z.literal("")),
  description: z
    .string()
    .max(5000, "Description must not exceed 5000 characters")
    .optional()
    .or(z.literal("")),
});

export type GenericStatusType = z.infer<typeof genericStatusTypeSchema>;
export type GenericStatusTypeFormData = z.infer<
  typeof genericStatusTypeFormSchema
>;
