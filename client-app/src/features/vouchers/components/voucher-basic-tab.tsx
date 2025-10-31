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
import type { VoucherFormData } from "../api/schema";
import type { Payee } from "@/features/payees/api/schema";
import type { GenericStatusType } from "@/features/generic-status-types/api/generic-status-type-api";

interface VoucherBasicTabProps {
  form: UseFormReturn<VoucherFormData>;
  payees: Payee[];
  paymentStatuses: GenericStatusType[];
}

export function VoucherBasicTab({
  form,
  payees,
  paymentStatuses,
}: VoucherBasicTabProps) {
  return (
    <div className="space-y-4 mt-4">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="voucherNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Voucher Number *</FormLabel>
              <FormControl>
                <Input placeholder="Enter voucher number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="voucherDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Voucher Date *</FormLabel>
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
              <Select onValueChange={field.onChange} value={field.value || ""}>
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
