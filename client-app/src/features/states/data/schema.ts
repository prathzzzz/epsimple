import { z } from 'zod'

/**
 * Helper function to capitalize the first letter of each word in a string
 * Example: "new delhi" -> "New Delhi"
 * Example: "uttar pradesh" -> "Uttar Pradesh"
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
 * Example: "mh" -> "MH"
 */
export function toUpperCase(input: string): string {
  if (!input) return input
  return input.trim().toUpperCase()
}

// Zod schema for state validation
export const stateSchema = z.object({
  id: z.number(),
  stateName: z.string(),
  stateCode: z.string(),
  stateCodeAlt: z.string().nullable().optional(),
  createdAt: z.string(),
})

// Schema for form data with transformations
export const stateFormSchema = z.object({
  stateName: z
    .string()
    .min(1, 'State name is required')
    .max(100, 'State name cannot exceed 100 characters')
    .transform(capitalizeWords),
  stateCode: z
    .string()
    .min(2, 'State code must be at least 2 characters')
    .max(10, 'State code cannot exceed 10 characters')
    .regex(/^[A-Za-z0-9_-]+$/, 'State code can only contain letters, numbers, hyphens and underscores')
    .transform(toUpperCase),
  stateCodeAlt: z
    .string()
    .max(10, 'Alternate state code cannot exceed 10 characters')
    .regex(/^[A-Za-z0-9_-]*$/, 'Alternate state code can only contain letters, numbers, hyphens and underscores')
    .optional()
    .or(z.literal(''))
    .transform(val => val ? toUpperCase(val) : val),
})

// Type exports
export type State = z.infer<typeof stateSchema>
export type StateFormData = z.infer<typeof stateFormSchema>
