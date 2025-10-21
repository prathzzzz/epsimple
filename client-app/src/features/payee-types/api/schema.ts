import { z } from "zod";

export const payeeTypeSchema = z.object({
  id: z.number(),
  payeeType: z.string(),
  payeeCategory: z.string().nullable(),
  description: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const payeeTypeFormSchema = z.object({
  payeeType: z
    .string()
    .min(1, "Payee type is required")
    .max(50, "Payee type must not exceed 50 characters"),
  payeeCategory: z
    .string()
    .max(100, "Payee category must not exceed 100 characters")
    .optional(),
  description: z
    .string()
    .max(5000, "Description must not exceed 5000 characters")
    .optional(),
});

export type PayeeType = z.infer<typeof payeeTypeSchema>;
export type PayeeTypeFormData = z.infer<typeof payeeTypeFormSchema>;
