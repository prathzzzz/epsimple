import { z } from 'zod';

export const payeeSchema = z.object({
  payeeTypeId: z.number().min(1, 'Payee type is required'),
  payeeDetailsId: z.number().min(1, 'Payee details are required'),
  vendorId: z.number().nullable().optional(),
  landlordId: z.number().nullable().optional(),
});

export type PayeeFormData = z.infer<typeof payeeSchema>;

export interface Payee {
  id: number;
  payeeTypeId: number;
  payeeTypeName: string;
  payeeDetailsId: number;
  payeeName: string;
  accountNumber: string;
  bankName: string | null;
  vendorId: number | null;
  vendorName: string | null;
  landlordId: number | null;
  landlordName: string | null;
  createdAt: string;
  updatedAt: string;
}
