import type { UseFormReturn } from "react-hook-form";
import { format } from "date-fns";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { DatePicker } from "@/components/date-picker";
import type { InvoiceFormData } from "../api/schema";
import type { Payee } from "@/features/payees/api/schema";
import type { GenericStatusType } from "@/features/generic-status-types/api/generic-status-type-api";
import type { PaymentDetails } from "@/features/payment-details/api/schema";

interface InvoiceBasicTabProps {
  readonly form: UseFormReturn<InvoiceFormData>;
  readonly payees: Payee[];
  readonly paymentStatuses: GenericStatusType[];
  readonly paymentDetails: PaymentDetails[];
}

export function InvoiceBasicTab({ form, payees, paymentStatuses, paymentDetails }: InvoiceBasicTabProps) {
  return (
    <div className="space-y-4 mt-4">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="invoiceNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Invoice Number *</FormLabel>
              <FormControl>
                <Input placeholder="Enter invoice number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="invoiceDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Invoice Date *</FormLabel>
              <DatePicker
                selected={field.value ? new Date(field.value) : undefined}
                onSelect={(date) =>
                  field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                }
              />
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="payeeId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Payee *</FormLabel>
              <Select
                onValueChange={(value) => field.onChange(Number(value))}
                value={field.value > 0 ? String(field.value) : ""}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select payee" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {payees.map((payee) => (
                    <SelectItem key={payee.id} value={String(payee.id)}>
                      {payee.payeeName} ({payee.payeeTypeName})
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="paymentDetailsId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Payment Details</FormLabel>
              <Select
                onValueChange={(value) => field.onChange(value === "none" ? undefined : Number(value))}
                value={field.value ? String(field.value) : "none"}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select payment details (optional)" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  <SelectItem value="none">None</SelectItem>
                  {paymentDetails.map((payment) => (
                    <SelectItem key={payment.id} value={String(payment.id)}>
                      {payment.transactionNumber || `Payment ${payment.id}`} - {payment.paymentMethodName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="vendorName"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Vendor Name</FormLabel>
              <FormControl>
                <Input placeholder="Enter vendor name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="orderNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Order Number</FormLabel>
              <FormControl>
                <Input placeholder="Enter order number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="paymentStatus"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Payment Status</FormLabel>
              <Select
                onValueChange={field.onChange}
                value={field.value || ""}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select status" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {paymentStatuses.map((status) => (
                    <SelectItem key={status.id} value={status.statusName}>
                      {status.statusName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="invoiceReceivedDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Invoice Received Date</FormLabel>
              <DatePicker
                selected={field.value ? new Date(field.value) : undefined}
                onSelect={(date) =>
                  field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                }
              />
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="paymentDueDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Payment Due Date</FormLabel>
              <DatePicker
                selected={field.value ? new Date(field.value) : undefined}
                onSelect={(date) =>
                  field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                }
              />
              <FormMessage />
            </FormItem>
          )}
        />
      </div>
    </div>
  );
}
