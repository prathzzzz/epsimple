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

import { useVendorContext } from './vendor-provider';
import { vendorFormSchema, type VendorFormValues } from './schema';
import { useCreateVendor, useUpdateVendor } from '@/lib/vendors-api';
import { vendorTypesApi } from '@/features/vendor-types/api/vendor-types-api';
import { personDetailsApi } from '@/features/person-details/api/person-details-api';
import { useErrorHandler } from '@/hooks/use-error-handler';

export const VendorDrawer = () => {
  const { isDrawerOpen, closeDrawer, drawerMode, selectedVendor } = useVendorContext();
  
  // Fetch vendor types list
  const { data: vendorTypesResponse, isLoading: loadingVendorTypes } = useQuery({
    queryKey: ['vendor-types', 'list'],
    queryFn: () => vendorTypesApi.getList(),
  });
  const vendorTypes = vendorTypesResponse?.data || [];

  // Fetch person details list
  const { data: personDetailsResponse, isLoading: loadingPersonDetails } = useQuery({
    queryKey: ['person-details', 'list'],
    queryFn: () => personDetailsApi.getList(),
  });
  const personDetailsList = personDetailsResponse || [];

  const createVendor = useCreateVendor();
  const updateVendor = useUpdateVendor();
  const handleError = useErrorHandler();

  const form = useForm<VendorFormValues>({
    resolver: zodResolver(vendorFormSchema),
    defaultValues: {
      vendorTypeId: 0,
      vendorDetailsId: 0,
      vendorCodeAlt: '',
    },
  });

  useEffect(() => {
    if (drawerMode === 'edit' && selectedVendor) {
      form.reset({
        vendorTypeId: selectedVendor.vendorTypeId,
        vendorDetailsId: selectedVendor.vendorDetailsId,
        vendorCodeAlt: selectedVendor.vendorCodeAlt || '',
      });
    } else if (drawerMode === 'create') {
      form.reset({
        vendorTypeId: 0,
        vendorDetailsId: 0,
        vendorCodeAlt: '',
      });
    }
  }, [drawerMode, selectedVendor, form]);

  const onSubmit = async (data: VendorFormValues) => {
    try {
      // Clean up empty vendorCodeAlt
      const submitData = {
        ...data,
        vendorCodeAlt: data.vendorCodeAlt?.trim() || undefined,
      };

      if (drawerMode === 'create') {
        await createVendor.mutateAsync(submitData);
        toast.success('Vendor created successfully');
      } else if (selectedVendor) {
        await updateVendor.mutateAsync({
          id: selectedVendor.id,
          data: submitData,
        });
        toast.success('Vendor updated successfully');
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

  const isLoading = createVendor.isPending || updateVendor.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col sm:max-w-[540px]">
        <SheetHeader className="text-start">
          <SheetTitle>
            {drawerMode === 'create' ? 'Create New Vendor' : 'Edit Vendor'}
          </SheetTitle>
          <SheetDescription>
            {drawerMode === 'create'
              ? 'Add a new vendor to the system'
              : 'Update vendor information'}
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form 
            id="vendor-form"
            onSubmit={form.handleSubmit(onSubmit)} 
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="vendorTypeId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vendor Type *</FormLabel>
                  <FormControl>
                    <SelectDropdown
                      defaultValue={field.value === 0 ? '' : field.value.toString()}
                      onValueChange={(value) => field.onChange(Number(value))}
                      placeholder="Select vendor type"
                      items={vendorTypes.map((type: any) => ({
                        label: `${type.typeName}${type.vendorCategory?.categoryName ? ` (${type.vendorCategory.categoryName})` : ''}`,
                        value: type.id.toString(),
                      }))}
                      disabled={loadingVendorTypes || isLoading}
                      isControlled
                    />
                  </FormControl>
                  <FormDescription>
                    Select the type of vendor (e.g., Supplier, Contractor)
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="vendorDetailsId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vendor Details *</FormLabel>
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
                    Select the person details for this vendor
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="vendorCodeAlt"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vendor Code (Optional)</FormLabel>
                  <FormControl>
                    <Input
                      {...field}
                      placeholder="e.g., VEN-001"
                      disabled={isLoading}
                      maxLength={10}
                      onChange={(e) => {
                        field.onChange(e.target.value.toUpperCase());
                      }}
                    />
                  </FormControl>
                  <FormDescription>
                    Optional unique code (1-10 uppercase alphanumeric characters)
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
            form="vendor-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {drawerMode === 'create' ? 'Create Vendor' : 'Update Vendor'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
};
