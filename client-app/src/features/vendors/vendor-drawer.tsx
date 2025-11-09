import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { toast } from 'sonner';
import { Loader2, Check, ChevronsUpDown } from 'lucide-react';

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
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { Command, CommandInput, CommandList, CommandEmpty, CommandItem } from '@/components/ui/command';
import { cn } from '@/lib/utils';

import { useVendorContext } from './vendor-provider';
import { vendorFormSchema, type VendorFormValues } from './schema';
import { useCreateVendor, useUpdateVendor } from '@/features/vendors/api/vendors-api';
import { vendorTypesApi } from '@/features/vendor-types/api/vendor-types-api';
import { personDetailsApi } from '@/features/person-details/api/person-details-api';
import { useErrorHandler } from '@/hooks/use-error-handler';

export const VendorDrawer = () => {
  const { isDrawerOpen, closeDrawer, drawerMode, selectedVendor } = useVendorContext();
  
  const [vendorTypeSearch, setVendorTypeSearch] = useState("");
  const [vendorTypeOpen, setVendorTypeOpen] = useState(false);
  const [personDetailsSearch, setPersonDetailsSearch] = useState("");
  const [personDetailsOpen, setPersonDetailsOpen] = useState(false);
  
  // Fetch vendor types with search
  const { data: vendorTypes = [], isLoading: loadingVendorTypes } = vendorTypesApi.useSearch(vendorTypeSearch);
  
  // Fetch person details with search
  const { data: personDetailsList = [], isLoading: loadingPersonDetails } = personDetailsApi.useSearch(personDetailsSearch);

  // Fetch initial items for display
  const { data: allVendorTypes = [] } = vendorTypesApi.useSearch("");
  const { data: allPersonDetails = [] } = personDetailsApi.useSearch("");

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

  // Combine search results with selected items
  const displayVendorTypes = (() => {
    if (!selectedVendor?.vendorTypeId) return vendorTypes;
    const selected = allVendorTypes.find(t => t.id === selectedVendor.vendorTypeId);
    if (!selected) return vendorTypes;
    if (vendorTypes.some(t => t.id === selected.id)) return vendorTypes;
    return [selected, ...vendorTypes];
  })();

  const displayPersonDetails = (() => {
    if (!selectedVendor?.vendorDetailsId) return personDetailsList;
    const selected = allPersonDetails.find(p => p.id === selectedVendor.vendorDetailsId);
    if (!selected) return personDetailsList;
    if (personDetailsList.some(p => p.id === selected.id)) return personDetailsList;
    return [selected, ...personDetailsList];
  })();

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
                  <Popover open={vendorTypeOpen} onOpenChange={setVendorTypeOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={vendorTypeOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayVendorTypes.find((t) => t.id === field.value)?.typeName || "Select vendor type"
                            : "Select vendor type"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search vendor types..."
                          value={vendorTypeSearch}
                          onValueChange={setVendorTypeSearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {loadingVendorTypes ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No vendor type found."
                            )}
                          </CommandEmpty>
                          {displayVendorTypes.map((type) => (
                            <CommandItem
                              key={type.id}
                              value={String(type.id)}
                              onSelect={() => {
                                field.onChange(type.id);
                                setVendorTypeOpen(false);
                                setVendorTypeSearch("");
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  field.value === type.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {type.typeName}
                              {type.vendorCategory?.categoryName && (
                                <span className="ml-2 text-muted-foreground">
                                  ({type.vendorCategory.categoryName})
                                </span>
                              )}
                            </CommandItem>
                          ))}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
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
                  <Popover open={personDetailsOpen} onOpenChange={setPersonDetailsOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={personDetailsOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? (() => {
                                const person = displayPersonDetails.find((p) => p.id === field.value);
                                if (!person) return "Select person details";
                                const fullName = person.fullName || [
                                  person.firstName,
                                  person.middleName,
                                  person.lastName,
                                ]
                                  .filter(Boolean)
                                  .join(' ') || 'Unknown';
                                return `${fullName}${person.contactNumber ? ` (${person.contactNumber})` : ''}`;
                              })()
                            : "Select person details"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search person details..."
                          value={personDetailsSearch}
                          onValueChange={setPersonDetailsSearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {loadingPersonDetails ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No person details found."
                            )}
                          </CommandEmpty>
                          {displayPersonDetails.map((person) => {
                            const fullName = person.fullName || [
                              person.firstName,
                              person.middleName,
                              person.lastName,
                            ]
                              .filter(Boolean)
                              .join(' ') || 'Unknown';
                            return (
                              <CommandItem
                                key={person.id}
                                value={String(person.id)}
                                onSelect={() => {
                                  field.onChange(person.id);
                                  setPersonDetailsOpen(false);
                                  setPersonDetailsSearch("");
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    field.value === person.id ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {fullName}
                                {person.contactNumber && (
                                  <span className="ml-2 text-muted-foreground">
                                    ({person.contactNumber})
                                  </span>
                                )}
                              </CommandItem>
                            );
                          })}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
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
