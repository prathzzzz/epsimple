import { z } from "zod";

export const expendituresVoucherSchema = z.object({
  id: z.number(),
  costItemId: z.number(),
  costItemFor: z.string(),
  costTypeName: z.string(),
  costCategoryName: z.string(),
  voucherId: z.number(),
  voucherNumber: z.string(),
  voucherDate: z.string(),
  payeeName: z.string(),
  managedProjectId: z.number(),
  projectName: z.string(),
  projectCode: z.string().optional(),
  bankName: z.string(),
  incurredDate: z.string().optional(),
  description: z.string().optional(),
  // claimAmount removed
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string().optional(),
  updatedBy: z.string().optional(),
});

export const expendituresVoucherFormSchema = z.object({
  costItemId: z.number({
    message: "Cost item is required",
  }),
  voucherId: z.number({
    message: "Voucher is required",
  }),
  managedProjectId: z.number({
    message: "Project is required",
  }),
  incurredDate: z.string().optional().or(z.literal("")),
  description: z.string().max(5000).optional().or(z.literal("")),
  // claimAmount removed
});

export type ExpendituresVoucher = z.infer<typeof expendituresVoucherSchema>;
export type ExpendituresVoucherFormData = z.infer<typeof expendituresVoucherFormSchema>;
