import { z } from "zod";

export const cityFormSchema = z.object({
  cityName: z
    .string()
    .min(1, "City name is required")
    .max(100, "City name cannot exceed 100 characters"),
  cityCode: z
    .string()
    .max(10, "City code cannot exceed 10 characters")
    .regex(
      /^[A-Za-z0-9_-]*$/,
      "City code can only contain letters, numbers, hyphens and underscores"
    )
    .optional()
    .or(z.literal("")),
  stateId: z.number().min(1, "State is required"),
});

export const cityResponseSchema = z.object({
  id: z.number(),
  cityName: z.string(),
  cityCode: z.string().nullable().optional(),
  stateId: z.number(),
  stateName: z.string(),
  stateCode: z.string().nullable().optional(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string().nullable().optional(),
  updatedBy: z.string().nullable().optional(),
});

export type CityFormData = z.infer<typeof cityFormSchema>;
export type City = z.infer<typeof cityResponseSchema>;
