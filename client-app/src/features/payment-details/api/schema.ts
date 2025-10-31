import { z } from "zod";

export const paymentDetailsSchema = z.object({
  id: z.number(),
  paymentMethodId: z.number(),
  paymentMethodName: z.string(),
  paymentDate: z.string(),
  paymentAmount: z.number(),
  transactionNumber: z.string().nullable(),
  vpa: z.string().nullable(),
  beneficiaryName: z.string().nullable(),
  beneficiaryAccountNumber: z.string().nullable(),
  paymentRemarks: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const paymentDetailsFormSchema = z.object({
  paymentMethodId: z
    .number()
    .min(1, "Payment method is required"),
  paymentDate: z
    .string()
    .min(1, "Payment date is required"),
  paymentAmount: z
    .number()
    .min(0.01, "Payment amount must be greater than zero"),
  transactionNumber: z
    .string()
    .max(255, "Transaction number must not exceed 255 characters")
    .optional(),
  vpa: z
    .string()
    .max(255, "VPA must not exceed 255 characters")
    .optional(),
  beneficiaryName: z
    .string()
    .max(255, "Beneficiary name must not exceed 255 characters")
    .optional(),
  beneficiaryAccountNumber: z
    .string()
    .max(255, "Beneficiary account number must not exceed 255 characters")
    .optional(),
  paymentRemarks: z
    .string()
    .max(5000, "Payment remarks must not exceed 5000 characters")
    .optional(),
});

export type PaymentDetails = z.infer<typeof paymentDetailsSchema>;
export type PaymentDetailsFormData = z.infer<typeof paymentDetailsFormSchema>;
