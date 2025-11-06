import type { UseFormReturn } from "react-hook-form";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { TabsContent } from "@/components/ui/tabs";
import type { SiteFormData } from "../../api/schema";

interface TechnicalTabProps {
  form: UseFormReturn<SiteFormData>;
}

export function TechnicalTab({ form }: TechnicalTabProps) {
  return (
    <TabsContent value="technical" className="flex-1 space-y-6 overflow-y-auto px-4 mt-0 data-[state=active]:mt-6">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="tssDocket"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>TSS Docket</FormLabel>
              <FormControl>
                <Input placeholder="Enter TSS docket" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="otcActivationStatus"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>OTC Activation Status</FormLabel>
              <FormControl>
                <Input placeholder="Enter OTC activation status" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <FormField
        control={form.control}
        name="craName"
        render={({ field }) => (
          <FormItem>
            <FormLabel>CRA Name</FormLabel>
            <FormControl>
              <Input placeholder="Enter CRA name" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />
    </TabsContent>
  );
}
