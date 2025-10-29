import { UseFormReturn } from "react-hook-form";
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

interface CassettesTabProps {
  form: UseFormReturn<SiteFormData>;
}

export function CassettesTab({ form }: CassettesTabProps) {
  return (
    <TabsContent value="cassettes" className="flex-1 space-y-6 overflow-y-auto px-4 mt-0 data-[state=active]:mt-6">
      <FormField
        control={form.control}
        name="cassetteSwapStatus"
        render={({ field }) => (
          <FormItem>
            <FormLabel>Cassette Swap Status</FormLabel>
            <FormControl>
              <Input placeholder="Enter cassette swap status" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="cassetteType1"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Cassette Type 1</FormLabel>
              <FormControl>
                <Input placeholder="Enter cassette type 1" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="cassetteType2"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Cassette Type 2</FormLabel>
              <FormControl>
                <Input placeholder="Enter cassette type 2" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="cassetteType3"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Cassette Type 3</FormLabel>
              <FormControl>
                <Input placeholder="Enter cassette type 3" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="cassetteType4"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Cassette Type 4</FormLabel>
              <FormControl>
                <Input placeholder="Enter cassette type 4" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>
    </TabsContent>
  );
}
