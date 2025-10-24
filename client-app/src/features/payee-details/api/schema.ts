import { z } from 'zod';

// PayeeDetails interface
export interface PayeeDetails {
  id: number;
  payeeName: string;
  panNumber?: string;
  aadhaarNumber?: string;
  bankId?: number;
  bankName?: string;
  ifscCode?: string;
  beneficiaryName?: string;
  accountNumber?: string;
  createdAt?: string;
  updatedAt?: string;
}

// Validation schema
export const payeeDetailsSchema = z.object({
  payeeName: z.string().min(1, 'Payee name is required').max(255, 'Payee name cannot exceed 255 characters'),
  panNumber: z
    .string()
    .optional()
    .refine(
      (val) => !val || /^[A-Z]{5}[0-9]{4}[A-Z]$/.test(val),
      'PAN number must be in format: AAAAA9999A (e.g., ABCDE1234F)'
    ),
  aadhaarNumber: z
    .string()
    .optional()
    .refine(
      (val) => !val || /^[0-9]{12}$/.test(val),
      'Aadhaar number must be exactly 12 digits'
    ),
  bankId: z.number().optional().nullable(),
  ifscCode: z
    .string()
    .optional()
    .refine(
      (val) => !val || /^[A-Z]{4}0[A-Z0-9]{6}$/.test(val),
      'IFSC code must be in format: AAAA0999999 (e.g., SBIN0001234)'
    ),
  beneficiaryName: z
    .string()
    .max(255, 'Beneficiary name cannot exceed 255 characters')
    .optional(),
  accountNumber: z
    .string()
    .optional()
    .refine(
      (val) => !val || /^[0-9]{9,18}$/.test(val),
      'Account number must be between 9 to 18 digits'
    ),
});

export type PayeeDetailsFormData = z.infer<typeof payeeDetailsSchema>;
