import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';

import { cn } from '@/lib/utils';
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
import { useSearchPayeeDetails } from '@/features/payee-details/api/payee-details-api';
import { useSearchVendors } from '@/lib/vendors-api';
import { useSearchLandlords } from '@/lib/landlords-api';

export function PayeeDrawer() {
  const { isDrawerOpen, closeDrawer, selectedPayee } = usePayee();

  const [payeeTypeSearch, setPayeeTypeSearch] = useState('');
  const [payeeTypeOpen, setPayeeTypeOpen] = useState(false);
  const [payeeDetailsSearch, setPayeeDetailsSearch] = useState('');
  const [payeeDetailsOpen, setPayeeDetailsOpen] = useState(false);
  const [vendorSearch, setVendorSearch] = useState('');
  const [vendorOpen, setVendorOpen] = useState(false);
  const [landlordSearch, setLandlordSearch] = useState('');
  const [landlordOpen, setLandlordOpen] = useState(false);

  const createMutation = payeeApi.useCreate();
  const updateMutation = payeeApi.useUpdate();

  const { data: payeeTypes = [], isLoading: isPayeeTypesLoading } =
    payeeTypesApi.useSearch(payeeTypeSearch);

  const { data: payeeDetailsData, isLoading: isPayeeDetailsLoading } = useSearchPayeeDetails({
    searchTerm: payeeDetailsSearch,
    page: 0,
    size: 50,
    sortBy: 'payeeName',
    sortDirection: 'ASC',
  });

  const payeeDetailsList = payeeDetailsData?.content || [];

  const { data: vendors = [], isLoading: isVendorsLoading } = useSearchVendors(vendorSearch);

  const { data: landlords = [], isLoading: isLandlordsLoading } = useSearchLandlords(landlordSearch);

  // Fetch initial items for display
  const { data: allPayeeTypes = [] } = payeeTypesApi.useSearch("");
  const { data: allVendors = [] } = useSearchVendors("");
  const { data: allLandlords = [] } = useSearchLandlords("");

  // Combine search results with selected items
  const displayPayeeTypes = (() => {
    if (!selectedPayee?.payeeTypeId) return payeeTypes;
    const selected = allPayeeTypes.find(t => t.id === selectedPayee.payeeTypeId);
    if (!selected) return payeeTypes;
    if (payeeTypes.some(t => t.id === selected.id)) return payeeTypes;
    return [selected, ...payeeTypes];
  })();

  const displayVendors = (() => {
    if (!selectedPayee?.vendorId) return vendors;
    const selected = allVendors.find(v => v.id === selectedPayee.vendorId);
    if (!selected) return vendors;
    if (vendors.some(v => v.id === selected.id)) return vendors;
    return [selected, ...vendors];
  })();

  const displayLandlords = (() => {
    if (!selectedPayee?.landlordId) return landlords;
    const selected = allLandlords.find(l => l.id === selectedPayee.landlordId);
    if (!selected) return landlords;
    if (landlords.some(l => l.id === selected.id)) return landlords;
    return [selected, ...landlords];
  })();

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
                <FormItem className="flex flex-col">
                  <FormLabel>Payee Type *</FormLabel>
                  <Popover open={payeeTypeOpen} onOpenChange={setPayeeTypeOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={payeeTypeOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayPayeeTypes.find((t) => t.id === field.value)?.payeeType || "Select payee type"
                            : "Select payee type"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search payee types..."
                          value={payeeTypeSearch}
                          onValueChange={setPayeeTypeSearch}
                        />
                        <CommandList>
                          {isPayeeTypesLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayPayeeTypes.length === 0 ? (
                            <CommandEmpty>No payee type found.</CommandEmpty>
                          ) : (
                            displayPayeeTypes.map((type) => (
                              <CommandItem
                                key={type.id}
                                value={String(type.id)}
                                onSelect={() => {
                                  field.onChange(type.id);
                                  setPayeeTypeOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    type.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {type.payeeType}
                              </CommandItem>
                            ))
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
              name="payeeDetailsId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Payee Details *</FormLabel>
                  <Popover open={payeeDetailsOpen} onOpenChange={setPayeeDetailsOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={payeeDetailsOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? payeeDetailsList.find((d) => d.id === field.value)
                                ? `${payeeDetailsList.find((d) => d.id === field.value)?.payeeName} - ${payeeDetailsList.find((d) => d.id === field.value)?.accountNumber}`
                                : "Select payee details"
                            : "Select payee details"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search payee details..."
                          value={payeeDetailsSearch}
                          onValueChange={setPayeeDetailsSearch}
                        />
                        <CommandList>
                          {isPayeeDetailsLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : payeeDetailsList.length === 0 ? (
                            <CommandEmpty>No payee details found.</CommandEmpty>
                          ) : (
                            payeeDetailsList.map((details) => (
                              <CommandItem
                                key={details.id}
                                value={String(details.id)}
                                onSelect={() => {
                                  field.onChange(details.id);
                                  setPayeeDetailsOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    details.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {details.payeeName} - {details.accountNumber}
                              </CommandItem>
                            ))
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
                <FormItem className="flex flex-col">
                  <FormLabel>Vendor (Optional)</FormLabel>
                  <Popover open={vendorOpen} onOpenChange={setVendorOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={vendorOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayVendors.find((v) => v.id === field.value)?.vendorName || "Select vendor"
                            : "None"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search vendors..."
                          value={vendorSearch}
                          onValueChange={setVendorSearch}
                        />
                        <CommandList>
                          {isVendorsLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayVendors.length === 0 && vendorSearch.trim().length > 0 ? (
                            <CommandEmpty>No vendor found.</CommandEmpty>
                          ) : (
                            displayVendors.map((vendor) => (
                              <CommandItem
                                key={vendor.id}
                                value={String(vendor.id)}
                                onSelect={() => {
                                  field.onChange(vendor.id);
                                  setVendorOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    vendor.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {vendor.vendorName}
                              </CommandItem>
                            ))
                          )}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
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
                <FormItem className="flex flex-col">
                  <FormLabel>Landlord (Optional)</FormLabel>
                  <Popover open={landlordOpen} onOpenChange={setLandlordOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={landlordOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayLandlords.find((l) => l.id === field.value)?.landlordName || "Select landlord"
                            : "None"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search landlords..."
                          value={landlordSearch}
                          onValueChange={setLandlordSearch}
                        />
                        <CommandList>
                          {isLandlordsLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayLandlords.length === 0 && landlordSearch.trim().length > 0 ? (
                            <CommandEmpty>No landlord found.</CommandEmpty>
                          ) : (
                            displayLandlords.map((landlord) => (
                              <CommandItem
                                key={landlord.id}
                                value={String(landlord.id)}
                                onSelect={() => {
                                  field.onChange(landlord.id);
                                  setLandlordOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    landlord.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {landlord.landlordName}
                              </CommandItem>
                            ))
                          )}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
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
