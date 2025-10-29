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

interface InfrastructureTabProps {
  form: UseFormReturn<SiteFormData>;
}

export function InfrastructureTab({ form }: InfrastructureTabProps) {
  return (
    <TabsContent value="infrastructure" className="flex-1 space-y-6 overflow-y-auto px-4 mt-0 data-[state=active]:mt-6">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="groutingStatus"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Grouting Status</FormLabel>
              <FormControl>
                <Input placeholder="Enter grouting status" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="itStabilizer"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>IT Stabilizer</FormLabel>
              <FormControl>
                <Input placeholder="Enter IT stabilizer info" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="rampStatus"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Ramp Status</FormLabel>
              <FormControl>
                <Input placeholder="Enter ramp status" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="upsBatteryBackupCapacity"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>UPS Battery Backup Capacity</FormLabel>
              <FormControl>
                <Input placeholder="Enter UPS capacity" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="connectivityType"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Connectivity Type</FormLabel>
              <FormControl>
                <Input placeholder="Enter connectivity type" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="acUnits"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>AC Units</FormLabel>
              <FormControl>
                <Input placeholder="Enter AC units info" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="mainDoorGlassWidth"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Main Door Glass Width (m)</FormLabel>
              <FormControl>
                <Input
                  type="number"
                  step="0.01"
                  placeholder="Enter width"
                  {...field}
                  value={field.value ?? ""}
                  onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : null)}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="fixedGlassWidth"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Fixed Glass Width (m)</FormLabel>
              <FormControl>
                <Input
                  type="number"
                  step="0.01"
                  placeholder="Enter width"
                  {...field}
                  value={field.value ?? ""}
                  onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : null)}
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
          name="signboardSize"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Signboard Size</FormLabel>
              <FormControl>
                <Input placeholder="Enter signboard size" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="brandingSize"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Branding Size</FormLabel>
              <FormControl>
                <Input placeholder="Enter branding size" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>
    </TabsContent>
  );
}
