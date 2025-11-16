import { z } from "zod";

export const expendituresInvoiceSchema = z.object({
  id: z.number(),
  costItemId: z.number(),
  costItemFor: z.string(),
  costTypeName: z.string(),
  costCategoryName: z.string(),
  invoiceId: z.number(),
  invoiceNumber: z.string(),
  invoiceDate: z.string(),
  payeeName: z.string(),
  managedProjectId: z.number(),
  projectName: z.string(),
  projectCode: z.string().optional(),
  bankName: z.string(),
  incurredDate: z.string().optional(),
  description: z.string().optional(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string().optional(),
  updatedBy: z.string().optional(),
});

export const expendituresInvoiceFormSchema = z.object({
  costItemId: z.number({
    message: "Cost item is required",
  }),
  invoiceId: z.number({
    message: "Invoice is required",
  }),
  managedProjectId: z.number({
    message: "Project is required",
  }),
  incurredDate: z.string().optional().or(z.literal("")),
  description: z.string().max(5000).optional().or(z.literal("")),
});

export type ExpendituresInvoice = z.infer<typeof expendituresInvoiceSchema>;
export type ExpendituresInvoiceFormData = z.infer<typeof expendituresInvoiceFormSchema>;

