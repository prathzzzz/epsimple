import type { UseFormReturn } from "react-hook-form";
import { format } from "date-fns";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { DatePicker } from "@/components/date-picker";
import type { InvoiceFormData } from "../api/schema";

interface InvoiceOtherTabProps {
  form: UseFormReturn<InvoiceFormData>;
}

export function InvoiceOtherTab({ form }: InvoiceOtherTabProps) {
  return (
    <div className="space-y-4 mt-4">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="machineSerialNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Machine Serial Number</FormLabel>
              <FormControl>
                <Input placeholder="Enter machine serial number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="paidDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Paid Date</FormLabel>
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
          name="masterPoNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Master PO Number</FormLabel>
              <FormControl>
                <Input placeholder="Enter master PO number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="masterPoDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Master PO Date</FormLabel>
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
          name="dispatchOrderNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Dispatch Order Number</FormLabel>
              <FormControl>
                <Input placeholder="Enter dispatch order number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="dispatchOrderDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Dispatch Order Date</FormLabel>
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
          name="utrDetail"
          render={({ field }) => (
            <FormItem>
              <FormLabel>UTR Detail</FormLabel>
              <FormControl>
                <Input placeholder="Enter UTR detail" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="billedByVendorGst"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Billed by Vendor (Vendor State GST No.)</FormLabel>
              <FormControl>
                <Input placeholder="Enter vendor GST number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="billedToEpsGst"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Billed to EPS (EPS State GST No.)</FormLabel>
              <FormControl>
                <Input placeholder="Enter EPS GST number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <FormField
        control={form.control}
        name="remarks"
        render={({ field }) => (
          <FormItem>
            <FormLabel>Remarks</FormLabel>
            <FormControl>
              <Textarea
                placeholder="Enter remarks"
                className="resize-none"
                rows={4}
                {...field}
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />
    </div>
  );
}
