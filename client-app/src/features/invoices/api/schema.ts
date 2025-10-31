import { z } from "zod";

export const invoiceSchema = z.object({
  id: z.number(),
  invoiceNumber: z.string(),
  invoiceDate: z.string(),
  invoiceReceivedDate: z.string().nullable(),
  orderNumber: z.string().nullable(),
  vendorName: z.string().nullable(),
  
  payeeId: z.number(),
  payeeName: z.string(),
  payeeTypeName: z.string(),
  
  paymentDetailsId: z.number().nullable(),
  transactionNumber: z.string().nullable(),
  
  paymentDueDate: z.string().nullable(),
  paymentStatus: z.string().nullable(),
  
  quantity: z.number().nullable(),
  unit: z.string().nullable(),
  unitPrice: z.number().nullable(),
  
  taxCgstPercentage: z.number().nullable(),
  taxSgstPercentage: z.number().nullable(),
  taxIgstPercentage: z.number().nullable(),
  
  basicAmount: z.number().nullable(),
  cgst: z.number().nullable(),
  sgst: z.number().nullable(),
  igst: z.number().nullable(),
  
  amount1: z.number().nullable(),
  amount2: z.number().nullable(),
  
  discountPercentage: z.number().nullable(),
  discountAmount: z.number().nullable(),
  
  tds: z.number().nullable(),
  advanceAmount: z.number().nullable(),
  totalAmount: z.number().nullable(),
  totalInvoiceValue: z.number().nullable(),
  netPayable: z.number().nullable(),
  
  paidDate: z.string().nullable(),
  
  machineSerialNumber: z.string().nullable(),
  masterPoNumber: z.string().nullable(),
  masterPoDate: z.string().nullable(),
  dispatchOrderNumber: z.string().nullable(),
  dispatchOrderDate: z.string().nullable(),
  
  utrDetail: z.string().nullable(),
  billedByVendorGst: z.string().nullable(),
  billedToEpsGst: z.string().nullable(),
  
  remarks: z.string().nullable(),
  
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const invoiceFormSchema = z.object({
  invoiceNumber: z
    .string()
    .min(1, "Invoice number is required")
    .max(100, "Invoice number must not exceed 100 characters"),
  invoiceDate: z
    .string()
    .min(1, "Invoice date is required"),
  invoiceReceivedDate: z.string().optional(),
  orderNumber: z.string().max(100, "Order number must not exceed 100 characters").optional(),
  vendorName: z.string().max(255, "Vendor name must not exceed 255 characters").optional(),
  
  payeeId: z
    .number()
    .min(1, "Payee is required"),
  paymentDetailsId: z.number().optional(),
  
  paymentDueDate: z.string().optional(),
  paymentStatus: z.string().max(20, "Payment status must not exceed 20 characters").optional(),
  
  quantity: z.number().optional(),
  unit: z.string().max(50, "Unit must not exceed 50 characters").optional(),
  unitPrice: z.number().optional(),
  
  taxCgstPercentage: z.number().min(0).max(100).optional(),
  taxSgstPercentage: z.number().min(0).max(100).optional(),
  taxIgstPercentage: z.number().min(0).max(100).optional(),
  
  basicAmount: z.number().optional(),
  cgst: z.number().optional(),
  sgst: z.number().optional(),
  igst: z.number().optional(),
  
  amount1: z.number().optional(),
  amount2: z.number().optional(),
  
  discountPercentage: z.number().min(0).max(100).optional(),
  discountAmount: z.number().optional(),
  
  tds: z.number().optional(),
  advanceAmount: z.number().optional(),
  totalAmount: z.number().optional(),
  totalInvoiceValue: z.number().optional(),
  netPayable: z.number().optional(),
  
  paidDate: z.string().optional(),
  
  machineSerialNumber: z.string().max(100).optional(),
  masterPoNumber: z.string().max(100).optional(),
  masterPoDate: z.string().optional(),
  dispatchOrderNumber: z.string().max(100).optional(),
  dispatchOrderDate: z.string().optional(),
  
  utrDetail: z.string().max(255).optional(),
  billedByVendorGst: z.string().max(100).optional(),
  billedToEpsGst: z.string().max(100).optional(),
  
  remarks: z.string().max(5000).optional(),
});

export type Invoice = z.infer<typeof invoiceSchema>;
export type InvoiceFormData = z.infer<typeof invoiceFormSchema>;
