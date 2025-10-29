import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2 } from 'lucide-react';
import { useQuery } from '@tanstack/react-query';

import { Button } from '@/components/ui/button';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
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
import { usePayee } from '../context/payee-provider';
import { payeeApi } from '../api/payee-api';
import { payeeSchema, type PayeeFormData } from '../api/schema';
import { payeeTypesApi } from '@/features/payee-types/api/payee-types-api';
import { type PayeeDetails } from '@/features/payee-details/api/schema';
import { payeeDetailsApi } from '@/features/payee-details/api/payee-details-api';
import { useVendorsList } from '@/lib/vendors-api';
import { useLandlordsList } from '@/lib/landlords-api';

export function PayeeDrawer() {
  const { isDrawerOpen, closeDrawer, selectedPayee } = usePayee();

  const createMutation = payeeApi.useCreate();
  const updateMutation = payeeApi.useUpdate();

  const { data: payeeTypesResponse } = useQuery({
    queryKey: ['payee-types', 'list'],
    queryFn: () => payeeTypesApi.getList(),
  });

  const { data: payeeDetailsResponse } = useQuery({
    queryKey: ['payee-details', 'list'],
    queryFn: () => payeeDetailsApi.getList(),
  });

  const { data: vendorsData } = useVendorsList();

  const { data: landlordsData } = useLandlordsList();

  const payeeTypes = payeeTypesResponse?.data || [];
  const payeeDetailsList: PayeeDetails[] = payeeDetailsResponse || [];
  const vendors = vendorsData || [];
  const landlords = landlordsData || [];

  const form = useForm<PayeeFormData>({
    resolver: zodResolver(payeeSchema),
    defaultValues: {
      payeeTypeId: 0,
      payeeDetailsId: 0,
      vendorId: null,
      landlordId: null,
    },
  });

  useEffect(() => {
    if (selectedPayee) {
      form.reset({
        payeeTypeId: selectedPayee.payeeTypeId,
        payeeDetailsId: selectedPayee.payeeDetailsId,
        vendorId: selectedPayee.vendorId || null,
        landlordId: selectedPayee.landlordId || null,
      });
    } else {
      form.reset({
        payeeTypeId: 0,
        payeeDetailsId: 0,
        vendorId: null,
        landlordId: null,
      });
    }
  }, [selectedPayee, form]);

  const onSubmit = async (data: PayeeFormData) => {
    const payload = {
      ...data,
      vendorId: data.vendorId || null,
      landlordId: data.landlordId || null,
    };

    if (selectedPayee) {
      updateMutation.mutate(
        {
          id: selectedPayee.id,
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
      <SheetContent className="flex flex-col sm:max-w-[500px]">
        <SheetHeader className="text-start">
          <SheetTitle>
            {selectedPayee ? 'Update' : 'Create'} Payee
          </SheetTitle>
          <SheetDescription>
            {selectedPayee
              ? 'Update the payee details.'
              : 'Add a new payee.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="payee-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="payeeTypeId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Payee Type *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a payee type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {payeeTypes.map((type) => (
                        <SelectItem key={type.id} value={String(type.id)}>
                          {type.payeeType}
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
              name="payeeDetailsId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Payee Details *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select payee details" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {payeeDetailsList.map((details) => (
                        <SelectItem key={details.id} value={String(details.id)}>
                          {details.payeeName} - {details.accountNumber}
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
                  <FormLabel>Vendor (Optional)</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(value === "none" ? null : Number(value))}
                    value={field.value ? String(field.value) : "none"}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a vendor" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="none">None</SelectItem>
                      {vendors.map((vendor) => (
                        <SelectItem key={vendor.id} value={String(vendor.id)}>
                          {vendor.vendorName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormDescription>
                    Link this payee to a vendor if applicable
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="landlordId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Landlord (Optional)</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(value === "none" ? null : Number(value))}
                    value={field.value ? String(field.value) : "none"}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a landlord" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="none">None</SelectItem>
                      {landlords.map((landlord) => (
                        <SelectItem key={landlord.id} value={String(landlord.id)}>
                          {landlord.landlordName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormDescription>
                    Link this payee to a landlord if applicable
                  </FormDescription>
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
            form="payee-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {selectedPayee ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
