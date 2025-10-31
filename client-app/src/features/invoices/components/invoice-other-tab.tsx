import type { UseFormReturn } from "react-hook-form";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Textarea } from "@/components/ui/textarea";
import type { InvoiceFormData } from "../api/schema";

interface InvoiceOtherTabProps {
  form: UseFormReturn<InvoiceFormData>;
}

export function InvoiceOtherTab({ form }: InvoiceOtherTabProps) {
  return (
    <div className="space-y-4 mt-4">
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
