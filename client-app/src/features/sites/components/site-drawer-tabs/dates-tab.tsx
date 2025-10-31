import type { UseFormReturn } from "react-hook-form";
import { format } from 'date-fns';
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { DatePicker } from '@/components/date-picker';
import { TabsContent } from "@/components/ui/tabs";
import type { SiteFormData } from "../../api/schema";

interface DatesTabProps {
  form: UseFormReturn<SiteFormData>;
}

export function DatesTab({ form }: DatesTabProps) {
  return (
    <TabsContent value="dates" className="flex-1 space-y-6 overflow-y-auto px-4 mt-0 data-[state=active]:mt-6">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="techLiveDate"
          render={({ field }) => (
            <FormItem className="flex flex-col flex-1">
              <FormLabel>Tech Live Date</FormLabel>
              <FormControl>
                <DatePicker
                  selected={field.value ? new Date(field.value) : undefined}
                  onSelect={(date) => {
                    field.onChange(date ? format(date, 'yyyy-MM-dd') : null);
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="cashLiveDate"
          render={({ field }) => (
            <FormItem className="flex flex-col flex-1">
              <FormLabel>Cash Live Date</FormLabel>
              <FormControl>
                <DatePicker
                  selected={field.value ? new Date(field.value) : undefined}
                  onSelect={(date) => {
                    field.onChange(date ? format(date, 'yyyy-MM-dd') : null);
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="siteCloseDate"
          render={({ field }) => (
            <FormItem className="flex flex-col flex-1">
              <FormLabel>Site Close Date</FormLabel>
              <FormControl>
                <DatePicker
                  selected={field.value ? new Date(field.value) : undefined}
                  onSelect={(date) => {
                    field.onChange(date ? format(date, 'yyyy-MM-dd') : null);
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="possessionDate"
          render={({ field }) => (
            <FormItem className="flex flex-col flex-1">
              <FormLabel>Possession Date</FormLabel>
              <FormControl>
                <DatePicker
                  selected={field.value ? new Date(field.value) : undefined}
                  onSelect={(date) => {
                    field.onChange(date ? format(date, 'yyyy-MM-dd') : null);
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="actualPossessionDate"
          render={({ field }) => (
            <FormItem className="flex flex-col flex-1">
              <FormLabel>Actual Possession Date</FormLabel>
              <FormControl>
                <DatePicker
                  selected={field.value ? new Date(field.value) : undefined}
                  onSelect={(date) => {
                    field.onChange(date ? format(date, 'yyyy-MM-dd') : null);
                  }}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>
    </TabsContent>
  );
}
