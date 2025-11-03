import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';
import { format } from 'date-fns';

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
import { DatePicker } from '@/components/date-picker';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { cn } from '@/lib/utils';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { useActivityWork } from '../context/activity-work-provider';
import { activityWorkApi } from '../api/activity-work-api';
import { activityWorkSchema, type ActivityWorkFormData } from '../api/schema';
import { activitiesApi } from '@/features/activities/api/activities-api';
import { useSearchVendors, type Vendor } from '@/features/vendors/api/vendors-api';
import { genericStatusTypeApi } from '@/features/generic-status-types/api/generic-status-type-api';

export function ActivityWorkDrawer() {
  const { isDrawerOpen, closeDrawer, selectedActivityWork } = useActivityWork();

  const [activitySearch, setActivitySearch] = useState('');
  const [activityOpen, setActivityOpen] = useState(false);
  const [vendorSearch, setVendorSearch] = useState('');
  const [vendorOpen, setVendorOpen] = useState(false);
  const [statusTypeSearch, setStatusTypeSearch] = useState('');
  const [statusTypeOpen, setStatusTypeOpen] = useState(false);

  const createMutation = activityWorkApi.useCreate();
  const updateMutation = activityWorkApi.useUpdate();

  const { data: activities = [], isLoading: isLoadingActivities } = 
    activitiesApi.useSearch(activitySearch);
  const { data: allActivities = [] } = activitiesApi.useSearch('');
  const { data: vendors = [], isLoading: isLoadingVendors } = 
    useSearchVendors(vendorSearch);
  const { data: statusTypes = [], isLoading: isLoadingStatusTypes } = 
    genericStatusTypeApi.useSearch(statusTypeSearch);

  // Display logic for activities dropdown
  const displayActivities = (() => {
    if (!selectedActivityWork?.activitiesId) return activities;
    const selectedActivity = allActivities.find((a) => a.id === selectedActivityWork.activitiesId);
    if (!selectedActivity || activities.some((a) => a.id === selectedActivity.id)) {
      return activities;
    }
    return [selectedActivity, ...activities];
  })();

  const form = useForm<ActivityWorkFormData>({
    resolver: zodResolver(activityWorkSchema),
    defaultValues: {
      activitiesId: 0,
      vendorId: 0,
      vendorOrderNumber: '',
      workOrderDate: '',
      workStartDate: '',
      workCompletionDate: '',
      statusTypeId: 0,
    },
  });

  useEffect(() => {
    if (selectedActivityWork) {
      form.reset({
        activitiesId: selectedActivityWork.activitiesId,
        vendorId: selectedActivityWork.vendorId,
        vendorOrderNumber: selectedActivityWork.vendorOrderNumber || '',
        workOrderDate: selectedActivityWork.workOrderDate || '',
        workStartDate: selectedActivityWork.workStartDate || '',
        workCompletionDate: selectedActivityWork.workCompletionDate || '',
        statusTypeId: selectedActivityWork.statusTypeId,
      });
    } else {
      form.reset({
        activitiesId: 0,
        vendorId: 0,
        vendorOrderNumber: '',
        workOrderDate: '',
        workStartDate: '',
        workCompletionDate: '',
        statusTypeId: 0,
      });
    }
  }, [selectedActivityWork, form]);

  const onSubmit = async (data: ActivityWorkFormData) => {
    // Convert empty strings to undefined for optional fields
    const payload = {
      ...data,
      vendorOrderNumber: data.vendorOrderNumber || undefined,
      workOrderDate: data.workOrderDate || undefined,
      workStartDate: data.workStartDate || undefined,
      workCompletionDate: data.workCompletionDate || undefined,
    };

    if (selectedActivityWork) {
      updateMutation.mutate(
        {
          id: selectedActivityWork.id,
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
            {selectedActivityWork ? 'Update' : 'Create'} Activity Work
          </SheetTitle>
          <SheetDescription>
            {selectedActivityWork
              ? 'Update the activity work details.'
              : 'Add a new activity work order.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="activity-work-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="activitiesId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Activity *</FormLabel>
                  <Popover open={activityOpen} onOpenChange={setActivityOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          className={cn(
                            'w-full justify-between',
                            !field.value && 'text-muted-foreground'
                          )}
                        >
                          {field.value
                            ? displayActivities.find((a) => a.id === field.value)?.activityName
                            : 'Select an activity'}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-full p-0" align="start">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search activities..."
                          value={activitySearch}
                          onValueChange={setActivitySearch}
                        />
                        <CommandList>
                          {isLoadingActivities ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayActivities.length === 0 ? (
                            <CommandEmpty>No activities found.</CommandEmpty>
                          ) : (
                            <CommandGroup>
                              {displayActivities.map((activity) => (
                                <CommandItem
                                  key={activity.id}
                                  value={String(activity.id)}
                                  onSelect={() => {
                                    field.onChange(activity.id);
                                    setActivityOpen(false);
                                    setActivitySearch('');
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      'mr-2 h-4 w-4',
                                      activity.id === field.value ? 'opacity-100' : 'opacity-0'
                                    )}
                                  />
                                  {activity.activityName}
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          )}
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
              name="vendorId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vendor *</FormLabel>
                  <Popover open={vendorOpen} onOpenChange={setVendorOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          className={cn(
                            'w-full justify-between',
                            !field.value && 'text-muted-foreground'
                          )}
                        >
                          {field.value
                            ? vendors.find((v: Vendor) => v.id === field.value)?.vendorName
                            : 'Select a vendor'}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-full p-0" align="start">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search vendors..."
                          value={vendorSearch}
                          onValueChange={setVendorSearch}
                        />
                        <CommandList>
                          {isLoadingVendors ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : vendors.length === 0 ? (
                            <CommandEmpty>No vendors found.</CommandEmpty>
                          ) : (
                            <CommandGroup>
                              {vendors.map((vendor: Vendor) => (
                                <CommandItem
                                  key={vendor.id}
                                  value={String(vendor.id)}
                                  onSelect={() => {
                                    field.onChange(vendor.id);
                                    setVendorOpen(false);
                                    setVendorSearch('');
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      'mr-2 h-4 w-4',
                                      vendor.id === field.value ? 'opacity-100' : 'opacity-0'
                                    )}
                                  />
                                  {vendor.vendorName}
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          )}
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
              name="vendorOrderNumber"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vendor Order Number</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Enter vendor order number (e.g., WO-2024-001)"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="workOrderDate"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Work Order Date</FormLabel>
                  <FormControl>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date) => {
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '');
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="workStartDate"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Work Start Date</FormLabel>
                  <FormControl>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date) => {
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '');
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="workCompletionDate"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Work Completion Date</FormLabel>
                  <FormControl>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date) => {
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '');
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="statusTypeId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Status Type *</FormLabel>
                  <Popover open={statusTypeOpen} onOpenChange={setStatusTypeOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          className={cn(
                            'w-full justify-between',
                            !field.value && 'text-muted-foreground'
                          )}
                        >
                          {field.value
                            ? statusTypes.find((s) => s.id === field.value)?.statusName
                            : 'Select a status type'}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-full p-0" align="start">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search status types..."
                          value={statusTypeSearch}
                          onValueChange={setStatusTypeSearch}
                        />
                        <CommandList>
                          {isLoadingStatusTypes ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : statusTypes.length === 0 ? (
                            <CommandEmpty>No status types found.</CommandEmpty>
                          ) : (
                            <CommandGroup>
                              {statusTypes.map((statusType) => (
                                <CommandItem
                                  key={statusType.id}
                                  value={String(statusType.id)}
                                  onSelect={() => {
                                    field.onChange(statusType.id);
                                    setStatusTypeOpen(false);
                                    setStatusTypeSearch('');
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      'mr-2 h-4 w-4',
                                      statusType.id === field.value ? 'opacity-100' : 'opacity-0'
                                    )}
                                  />
                                  {statusType.statusName}
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          )}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
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
            form="activity-work-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {selectedActivityWork ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
