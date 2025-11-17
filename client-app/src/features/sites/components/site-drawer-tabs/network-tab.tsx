import { type UseFormReturn } from "react-hook-form";
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

interface NetworkTabProps {
  form: UseFormReturn<SiteFormData>;
}

export function NetworkTab({ form }: NetworkTabProps) {
  return (
    <TabsContent value="network" className="flex-1 space-y-6 overflow-y-auto px-4 mt-0 data-[state=active]:mt-6">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="gatewayIp"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Gateway IP</FormLabel>
              <FormControl>
                <Input placeholder="192.168.1.1" {...field} className="font-mono" />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="atmIp"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>ATM IP</FormLabel>
              <FormControl>
                <Input placeholder="192.168.1.100" {...field} className="font-mono" />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="subnetMask"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Subnet Mask</FormLabel>
              <FormControl>
                <Input placeholder="255.255.255.0" {...field} className="font-mono" />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="natIp"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>NAT IP</FormLabel>
              <FormControl>
                <Input placeholder="192.168.1.1" {...field} className="font-mono" />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="switchIp"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Switch IP</FormLabel>
              <FormControl>
                <Input placeholder="192.168.1.254" {...field} className="font-mono" />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="tlsPort"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>TLS Port</FormLabel>
              <FormControl>
                <Input placeholder="443" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <FormField
        control={form.control}
        name="tlsDomainName"
        render={({ field }) => (
          <FormItem>
            <FormLabel>TLS Domain Name</FormLabel>
            <FormControl>
              <Input placeholder="Enter TLS domain name" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <FormField
        control={form.control}
        name="ejDocket"
        render={({ field }) => (
          <FormItem>
            <FormLabel>EJ Docket</FormLabel>
            <FormControl>
              <Input placeholder="Enter EJ docket" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />
    </TabsContent>
  );
}
