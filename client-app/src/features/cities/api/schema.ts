import { z } from "zod";

/**
 * Helper function to capitalize the first letter of each word in a string
 * Example: "new delhi" -> "New Delhi"
 * Example: "MUMBAI" -> "Mumbai"
 * Example: "pUnE" -> "Pune"
 */
export function capitalizeWords(input: string): string {
  if (!input) return input
  
  return input
    .trim()
    .split(/\s+/)
    .map(word => {
      if (!word) return word
      return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
    })
    .join(' ')
}

/**
 * Helper function to transform string to uppercase
 * Example: "mum" -> "MUM"
 */
export function toUpperCase(input: string): string {
  if (!input) return input
  return input.trim().toUpperCase()
}

export const cityFormSchema = z.object({
  cityName: z
    .string()
    .min(1, "City name is required")
    .max(100, "City name cannot exceed 100 characters")
    .transform(capitalizeWords),
  cityCode: z
    .string()
    .max(10, "City code cannot exceed 10 characters")
    .regex(
      /^[A-Za-z0-9_-]*$/,
      "City code can only contain letters, numbers, hyphens and underscores"
    )
    .optional()
    .or(z.literal(""))
    .transform(val => val?.trim() ? toUpperCase(val) : undefined),
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
