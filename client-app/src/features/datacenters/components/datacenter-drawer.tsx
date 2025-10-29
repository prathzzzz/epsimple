import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2 } from 'lucide-react';
import { useQuery } from '@tanstack/react-query';

import { Button } from '@/components/ui/button';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { useDatacenter } from '../context/datacenter-provider';
import { datacenterApi } from '../api/datacenter-api';
import { datacenterSchema, type DatacenterFormData } from '../api/schema';
import { locationApi } from '@/features/locations/api/location-api';

export function DatacenterDrawer() {
  const { isDrawerOpen, closeDrawer, selectedDatacenter } = useDatacenter();

  const createMutation = datacenterApi.useCreate();
  const updateMutation = datacenterApi.useUpdate();

  const { data: locationsResponse } = useQuery({
    queryKey: ['locations', 'list'],
    queryFn: () => locationApi.getList(),
  });

  const locations = locationsResponse || [];

  const form = useForm<DatacenterFormData>({
    resolver: zodResolver(datacenterSchema),
    defaultValues: {
      datacenterName: '',
      datacenterCode: '',
      datacenterType: '',
      locationId: 0,
    },
  });

  useEffect(() => {
    if (selectedDatacenter) {
      form.reset({
        datacenterName: selectedDatacenter.datacenterName,
        datacenterCode: selectedDatacenter.datacenterCode || '',
        datacenterType: selectedDatacenter.datacenterType || '',
        locationId: selectedDatacenter.locationId,
      });
    } else {
      form.reset({
        datacenterName: '',
        datacenterCode: '',
        datacenterType: '',
        locationId: 0,
      });
    }
  }, [selectedDatacenter, form]);

  const onSubmit = async (data: DatacenterFormData) => {
    // Convert empty strings to undefined for optional fields
    const payload = {
      ...data,
      datacenterCode: data.datacenterCode || undefined,
      datacenterType: data.datacenterType || undefined,
    };

    if (selectedDatacenter) {
      updateMutation.mutate(
        {
          id: selectedDatacenter.id,
          data: payload,
        },
        {
          onSuccess: () => {
            closeDrawer();
            form.reset();
          },
        }
      );
    } else {
      createMutation.mutate(payload, {
        onSuccess: () => {
          closeDrawer();
          form.reset();
        },
      });
    }
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={closeDrawer}>
      <SheetContent className="flex flex-col sm:max-w-[600px]">
        <SheetHeader className="text-start">
          <SheetTitle>
            {selectedDatacenter ? 'Update' : 'Create'} Datacenter
          </SheetTitle>
          <SheetDescription>
            {selectedDatacenter
              ? 'Update the datacenter details.'
              : 'Add a new datacenter.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="datacenter-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="locationId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Location *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a location" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {locations.map((location) => (
                        <SelectItem key={location.id} value={String(location.id)}>
                          {location.locationName} ({location.cityName}, {location.stateName})
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
              name="datacenterName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Datacenter Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter datacenter name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="datacenterCode"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Datacenter Code</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Enter datacenter code (e.g., DC-001)"
                      className="font-mono uppercase"
                      {...field}
                      onChange={(e) => field.onChange(e.target.value.toUpperCase())}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="datacenterType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Datacenter Type</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter datacenter type (e.g., Primary, Backup, Edge)" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 px-4">
          <Button
            type="button"
            variant="outline"
            onClick={closeDrawer}
            disabled={isLoading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            form="datacenter-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {selectedDatacenter ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
