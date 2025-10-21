import { z } from "zod";

export const paymentMethodSchema = z.object({
  id: z.number(),
  methodName: z.string(),
  description: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const paymentMethodFormSchema = z.object({
  methodName: z
    .string()
    .min(1, "Method name is required")
    .max(50, "Method name must not exceed 50 characters"),
  description: z
    .string()
    .max(5000, "Description must not exceed 5000 characters")
    .optional(),
});

export type PaymentMethod = z.infer<typeof paymentMethodSchema>;
export type PaymentMethodFormData = z.infer<typeof paymentMethodFormSchema>;
