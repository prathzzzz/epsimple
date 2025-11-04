import { z } from 'zod';

export const siteActivityWorkExpenditureSchema = z.object({
  siteId: z.number({ message: 'Site is required' }).positive('Site is required'),
  activityWorkId: z.number({ message: 'Activity Work is required' }).positive('Activity Work is required'),
  expendituresInvoiceId: z.number({ message: 'Expenditure Invoice is required' }).positive('Expenditure Invoice is required'),
});

export type SiteActivityWorkExpenditureFormData = z.infer<typeof siteActivityWorkExpenditureSchema>;

export interface SiteActivityWorkExpenditure {
  id: number;
  siteId: number;
  siteCode: string;
  siteName: string;
  activityWorkId: number;
  activityName: string;
  expendituresInvoiceId: number;
  invoiceNumber: string;
  amount: number;
  costItemName: string;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}
