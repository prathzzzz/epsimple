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

import { useLandlordContext } from './landlord-provider';
import { landlordFormSchema, type LandlordFormValues } from './schema';
import { useCreateLandlord, useUpdateLandlord } from '@/lib/landlords-api';
import { personDetailsApi } from '@/features/person-details/api/person-details-api';
import { useErrorHandler } from '@/hooks/use-error-handler';

export const LandlordDrawer = () => {
  const { isDrawerOpen, closeDrawer, drawerMode, selectedLandlord } = useLandlordContext();

  const [personDetailsSearch, setPersonDetailsSearch] = useState("");
  const [personDetailsOpen, setPersonDetailsOpen] = useState(false);

  // Fetch person details with search
  const { data: personDetailsList = [], isLoading: loadingPersonDetails } = personDetailsApi.useSearch(personDetailsSearch);

  // Fetch initial items for display
  const { data: allPersonDetails = [] } = personDetailsApi.useSearch("");

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

  // Combine search results with selected items
  const displayPersonDetails = (() => {
    if (!selectedLandlord?.landlordDetailsId) return personDetailsList;
    const selected = allPersonDetails.find(p => p.id === selectedLandlord.landlordDetailsId);
    if (!selected) return personDetailsList;
    if (personDetailsList.some(p => p.id === selected.id)) return personDetailsList;
    return [selected, ...personDetailsList];
  })();

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
                                return `${fullName}${person.email ? ` (${person.email})` : ''}`;
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
                                {person.email && (
                                  <span className="ml-2 text-muted-foreground">
                                    ({person.email})
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
