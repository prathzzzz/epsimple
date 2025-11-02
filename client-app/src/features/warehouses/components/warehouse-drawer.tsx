import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';

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
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Command, CommandInput, CommandList, CommandEmpty, CommandItem } from "@/components/ui/command";
import { cn } from "@/lib/utils";
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
  const [locationSearch, setLocationSearch] = useState("");
  const [locationOpen, setLocationOpen] = useState(false);

  const createMutation = warehouseApi.useCreate();
  const updateMutation = warehouseApi.useUpdate();

  const { data: locationsData, isLoading: isLoadingLocations } = locationApi.useSearch({
    searchTerm: locationSearch,
    page: 0,
    size: 50,
  });
  
  const locations = locationsData || [];
  
  // Fetch the selected location when editing to ensure it's displayed
  const { data: selectedLocationData } = locationApi.useSearch({
    searchTerm: "",
    page: 0,
    size: 20,
  });
  
  // Combine search results with selected location
  const displayLocations = (() => {
    if (!selectedWarehouse?.locationId) return locations;
    const selectedLocation = (selectedLocationData || []).find(l => l.id === selectedWarehouse.locationId);
    if (!selectedLocation) return locations;
    // Check if selected location is already in the locations list
    if (locations.some(l => l.id === selectedLocation.id)) return locations;
    // Add selected location to the top of the list
    return [selectedLocation, ...locations];
  })();

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
                  <Popover open={locationOpen} onOpenChange={setLocationOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={locationOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayLocations.find((l) => l.id === field.value)?.locationName || "Select location"
                            : "Select a location"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search locations..."
                          value={locationSearch}
                          onValueChange={setLocationSearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isLoadingLocations ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No location found."
                            )}
                          </CommandEmpty>
                          {displayLocations.map((location) => (
                            <CommandItem
                              key={location.id}
                              value={String(location.id)}
                              onSelect={() => {
                                field.onChange(location.id);
                                setLocationOpen(false);
                                setLocationSearch("");
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  field.value === location.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {location.locationName} ({location.cityName}, {location.stateName})
                            </CommandItem>
                          ))}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
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
