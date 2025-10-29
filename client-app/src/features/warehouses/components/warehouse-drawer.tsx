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
import { useWarehouse } from '../context/warehouse-provider';
import { warehouseApi } from '../api/warehouse-api';
import { warehouseFormSchema, type WarehouseFormData } from '../api/schema';
import { locationApi } from '@/features/locations/api/location-api';

export function WarehouseDrawer() {
  const { isDrawerOpen, closeDrawer, selectedWarehouse } = useWarehouse();

  const createMutation = warehouseApi.useCreate();
  const updateMutation = warehouseApi.useUpdate();

  const { data: locationsResponse } = useQuery({
    queryKey: ['locations', 'list'],
    queryFn: () => locationApi.getList(),
  });

  const locations = locationsResponse || [];

  const form = useForm<WarehouseFormData>({
    resolver: zodResolver(warehouseFormSchema),
    defaultValues: {
      warehouseName: '',
      warehouseCode: '',
      warehouseType: '',
      locationId: 0,
    },
  });

  useEffect(() => {
    if (selectedWarehouse) {
      form.reset({
        warehouseName: selectedWarehouse.warehouseName,
        warehouseCode: selectedWarehouse.warehouseCode || '',
        warehouseType: selectedWarehouse.warehouseType || '',
        locationId: selectedWarehouse.locationId,
      });
    } else {
      form.reset({
        warehouseName: '',
        warehouseCode: '',
        warehouseType: '',
        locationId: 0,
      });
    }
  }, [selectedWarehouse, form]);

  const onSubmit = async (data: WarehouseFormData) => {
    // Convert empty strings to undefined for optional fields
    const payload = {
      ...data,
      warehouseCode: data.warehouseCode || undefined,
      warehouseType: data.warehouseType || undefined,
    };

    if (selectedWarehouse) {
      updateMutation.mutate(
        {
          id: selectedWarehouse.id,
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
            {selectedWarehouse ? 'Update' : 'Create'} Warehouse
          </SheetTitle>
          <SheetDescription>
            {selectedWarehouse
              ? 'Update the warehouse details.'
              : 'Add a new warehouse.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="warehouse-form"
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
              name="warehouseName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Warehouse Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter warehouse name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="warehouseCode"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Warehouse Code</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Enter warehouse code (e.g., WH-001)"
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
              name="warehouseType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Warehouse Type</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter warehouse type (e.g., Main Storage, Cold Storage)" {...field} />
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
            form="warehouse-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {selectedWarehouse ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
