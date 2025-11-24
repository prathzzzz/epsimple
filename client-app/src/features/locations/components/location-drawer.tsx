import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';

import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Command,
  CommandEmpty,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { useLocation } from '../context/location-provider';
import { locationApi } from '../api/location-api';
import { locationFormSchema, type LocationFormData } from '../api/schema';
import { cityApi } from '@/features/cities/api/city-api';

export function LocationDrawer() {
  const { isDrawerOpen, closeDrawer, selectedLocation } = useLocation();

  const [citySearch, setCitySearch] = useState('');
  const [cityOpen, setCityOpen] = useState(false);

  const createMutation = locationApi.useCreate();
  const updateMutation = locationApi.useUpdate();

  const { data: cities = [], isLoading: isCitiesLoading } = cityApi.useSearch(citySearch);
  
  // Fetch the selected city when editing
  const { data: selectedCityData } = cityApi.useGetAll({
    page: 0,
    size: 1,
    search: selectedLocation?.cityId ? String(selectedLocation.cityId) : undefined,
  });
  
  // Combine search results with selected city
  const allCities = (() => {
    if (!selectedLocation?.cityId) return cities;
    const selectedCity = selectedCityData?.content.find(c => c.id === selectedLocation.cityId);
    if (!selectedCity) return cities;
    // Check if selected city is already in the cities list
    if (cities.some(c => c.id === selectedCity.id)) return cities;
    // Add selected city to the list
    return [selectedCity, ...cities];
  })();

  const form = useForm<LocationFormData>({
    resolver: zodResolver(locationFormSchema),
    defaultValues: {
      locationName: '',
      address: '',
      district: '',
      cityId: 0,
      pincode: '',
      region: '',
      zone: '',
      longitude: '',
      latitude: '',
    },
  });

  useEffect(() => {
    if (selectedLocation) {
      form.reset({
        locationName: selectedLocation.locationName,
        address: selectedLocation.address || '',
        district: selectedLocation.district || '',
        cityId: selectedLocation.cityId,
        pincode: selectedLocation.pincode || '',
        region: selectedLocation.region || '',
        zone: selectedLocation.zone || '',
        longitude: selectedLocation.longitude || '',
        latitude: selectedLocation.latitude || '',
      });
    } else {
      form.reset({
        locationName: '',
        address: '',
        district: '',
        cityId: 0,
        pincode: '',
        region: '',
        zone: '',
        longitude: '',
        latitude: '',
      });
    }
  }, [selectedLocation, form]);

  const onSubmit = async (data: LocationFormData) => {
    // Convert empty strings to undefined for optional fields
    const payload = {
      ...data,
      address: data.address || undefined,
      district: data.district || undefined,
      pincode: data.pincode ? data.pincode.trim() : undefined,
      region: data.region || undefined,
      zone: data.zone || undefined,
      longitude: data.longitude || undefined,
      latitude: data.latitude || undefined,
    };

    if (selectedLocation) {
      updateMutation.mutate(
        {
          id: selectedLocation.id,
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
            {selectedLocation ? 'Update' : 'Create'} Location
          </SheetTitle>
          <SheetDescription>
            {selectedLocation
              ? 'Update the location details.'
              : 'Add a new location.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="location-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="cityId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>City *</FormLabel>
                  <Popover open={cityOpen} onOpenChange={setCityOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={cityOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {(() => {
                            if (!field.value) return "Select city";
                            const selectedCity = allCities.find((c) => c.id === field.value);
                            return selectedCity 
                              ? `${selectedCity.cityName} (${selectedCity.stateName})`
                              : "Select city";
                          })()}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search cities..."
                          value={citySearch}
                          onValueChange={setCitySearch}
                        />
                        <CommandList>
                          {(() => {
                            if (isCitiesLoading) {
                              return (
                                <div className="flex items-center justify-center py-6">
                                  <Loader2 className="h-4 w-4 animate-spin" />
                                </div>
                              );
                            }
                            if (allCities.length === 0) {
                              return <CommandEmpty>No city found.</CommandEmpty>;
                            }
                            return allCities.map((city) => (
                              <CommandItem
                                key={city.id}
                                value={String(city.id)}
                                onSelect={() => {
                                  field.onChange(city.id);
                                  setCityOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    city.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {city.cityName} ({city.stateName})
                              </CommandItem>
                            ));
                          })()}
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
              name="locationName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Location Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter location name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="address"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Address</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter address"
                      className="min-h-[80px] resize-none"
                      rows={3}
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="district"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>District</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter district" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="pincode"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Pincode</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="Enter 6-digit pincode"
                        maxLength={6}
                        className="font-mono"
                        {...field}
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
                name="region"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Region</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter region" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="zone"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Zone</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter zone" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="latitude"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Latitude</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="e.g., 12.9349 or N 12° 56' 5.64''"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="longitude"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Longitude</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="e.g., 77.6212 or E 77° 37' 16.32''"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 gap-2 px-4 sm:space-x-0">
          <Button variant="outline" onClick={closeDrawer} disabled={isLoading}>
            Cancel
          </Button>
          <Button type="submit" form="location-form" disabled={isLoading}>
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {selectedLocation ? 'Update' : 'Save'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
