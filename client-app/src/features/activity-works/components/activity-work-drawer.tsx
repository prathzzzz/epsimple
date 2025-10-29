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
import { useActivityWork } from '../context/activity-work-provider';
import { activityWorkApi } from '../api/activity-work-api';
import { activityWorkSchema, type ActivityWorkFormData } from '../api/schema';
import { activitiesApi, type Activity } from '@/features/activities/api/activities-api';
import { useVendorsList, type Vendor } from '@/lib/vendors-api';
import { genericStatusTypeApi, type GenericStatusType } from '@/features/generic-status-types/api/generic-status-type-api';

export function ActivityWorkDrawer() {
  const { isDrawerOpen, closeDrawer, selectedActivityWork } = useActivityWork();

  const createMutation = activityWorkApi.useCreate();
  const updateMutation = activityWorkApi.useUpdate();

  const { data: activitiesResponse } = useQuery({
    queryKey: ['activities', 'list'],
    queryFn: () => activitiesApi.getList(),
  });

  const { data: vendorsData } = useVendorsList();

  const { data: statusTypesResponse } = useQuery({
    queryKey: ['generic-status-types', 'list'],
    queryFn: () => genericStatusTypeApi.getList(),
  });

  const activities: Activity[] = activitiesResponse?.data || [];
  const vendors: Vendor[] = vendorsData || [];
  const statusTypes: GenericStatusType[] = statusTypesResponse?.data || [];

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
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select an activity" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {activities.map((activity) => (
                        <SelectItem key={activity.id} value={String(activity.id)}>
                          {activity.activityName}
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
              name="vendorId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vendor *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a vendor" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {vendors.map((vendor) => (
                        <SelectItem key={vendor.id} value={String(vendor.id)}>
                          {vendor.vendorName}
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
                <FormItem>
                  <FormLabel>Work Order Date</FormLabel>
                  <FormControl>
                    <Input
                      type="date"
                      {...field}
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
                <FormItem>
                  <FormLabel>Work Start Date</FormLabel>
                  <FormControl>
                    <Input
                      type="date"
                      {...field}
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
                <FormItem>
                  <FormLabel>Work Completion Date</FormLabel>
                  <FormControl>
                    <Input
                      type="date"
                      {...field}
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
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a status type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {statusTypes.map((statusType) => (
                        <SelectItem key={statusType.id} value={String(statusType.id)}>
                          {statusType.statusName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
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
