import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { toast } from 'sonner';
import { Loader2 } from 'lucide-react';
import { useQuery } from '@tanstack/react-query';

import { Button } from '@/components/ui/button';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { SelectDropdown } from '@/components/select-dropdown';

import { useLandlordContext } from './landlord-provider';
import { landlordFormSchema, type LandlordFormValues } from './schema';
import { useCreateLandlord, useUpdateLandlord } from '@/lib/landlords-api';
import { personDetailsApi } from '@/features/person-details/api/person-details-api';
import { useErrorHandler } from '@/hooks/use-error-handler';

export const LandlordDrawer = () => {
  const { isDrawerOpen, closeDrawer, drawerMode, selectedLandlord } = useLandlordContext();

  // Fetch person details list
  const { data: personDetailsResponse, isLoading: loadingPersonDetails } = useQuery({
    queryKey: ['person-details', 'list'],
    queryFn: () => personDetailsApi.getList(),
  });
  const personDetailsList = personDetailsResponse || [];

  const createLandlord = useCreateLandlord();
  const updateLandlord = useUpdateLandlord();
  const handleError = useErrorHandler();

  const form = useForm<LandlordFormValues>({
    resolver: zodResolver(landlordFormSchema),
    defaultValues: {
      landlordDetailsId: 0,
      rentSharePercentage: undefined,
    },
  });

  useEffect(() => {
    if (drawerMode === 'edit' && selectedLandlord) {
      form.reset({
        landlordDetailsId: selectedLandlord.landlordDetailsId,
        rentSharePercentage: selectedLandlord.rentSharePercentage,
      });
    } else if (drawerMode === 'create') {
      form.reset({
        landlordDetailsId: 0,
        rentSharePercentage: undefined,
      });
    }
  }, [drawerMode, selectedLandlord, form]);

  const onSubmit = async (data: LandlordFormValues) => {
    try {
      if (drawerMode === 'create') {
        await createLandlord.mutateAsync(data);
        toast.success('Landlord created successfully');
      } else if (selectedLandlord) {
        await updateLandlord.mutateAsync({
          id: selectedLandlord.id,
          data,
        });
        toast.success('Landlord updated successfully');
      }
      closeDrawer();
      form.reset();
    } catch (error) {
      const { message } = handleError.handleError(error);
      toast.error(message);
    }
  };

  const handleClose = () => {
    closeDrawer();
    form.reset();
  };

  const isLoading = createLandlord.isPending || updateLandlord.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col sm:max-w-[540px]">
        <SheetHeader className="text-start">
          <SheetTitle>
            {drawerMode === 'create' ? 'Create New Landlord' : 'Edit Landlord'}
          </SheetTitle>
          <SheetDescription>
            {drawerMode === 'create'
              ? 'Add a new landlord to the system'
              : 'Update landlord information'}
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="landlord-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="landlordDetailsId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Landlord Details *</FormLabel>
                  <FormControl>
                    <SelectDropdown
                      defaultValue={field.value === 0 ? '' : field.value.toString()}
                      onValueChange={(value) => field.onChange(Number(value))}
                      placeholder="Select person details"
                      items={personDetailsList.map((person: any) => {
                        const fullName = person.fullName || [
                          person.firstName,
                          person.middleName,
                          person.lastName,
                        ]
                          .filter(Boolean)
                          .join(' ') || 'Unknown';
                        return {
                          label: `${fullName}${person.email ? ` (${person.email})` : ''}`,
                          value: person.id.toString(),
                        };
                      })}
                      disabled={loadingPersonDetails || isLoading}
                      isControlled
                    />
                  </FormControl>
                  <FormDescription>
                    Select the person details for this landlord
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="rentSharePercentage"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Rent Share Percentage (Optional)</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      type="number"
                      placeholder="e.g., 25.50"
                      disabled={isLoading}
                      min={0}
                      max={100}
                      step={0.01}
                      value={field.value ?? ''}
                      onChange={(e) => {
                        const value = e.target.value === '' ? undefined : parseFloat(e.target.value);
                        field.onChange(value);
                      }}
                    />
                  </FormControl>
                  <FormDescription>
                    Percentage of rent share (0-100)
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 gap-2 px-4 sm:space-x-0">
          <Button
            variant="outline"
            onClick={handleClose}
            disabled={isLoading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            form="landlord-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {drawerMode === 'create' ? 'Create Landlord' : 'Update Landlord'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
};
