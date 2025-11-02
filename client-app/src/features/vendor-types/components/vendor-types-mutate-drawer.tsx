import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

import { Button } from '@/components/ui/button';
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Command, CommandInput, CommandList, CommandEmpty, CommandItem } from "@/components/ui/command";
import { cn } from "@/lib/utils";

import { vendorTypesApi } from '../api/vendor-types-api';
import { vendorTypeFormSchema, type VendorTypeFormData, type VendorType } from '../api/schema';
import { vendorCategoriesApi } from '@/features/vendor-categories/api/vendor-categories-api';

interface VendorTypesMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: VendorType | null;
}

export function VendorTypesMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: VendorTypesMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;
  const [categorySearch, setCategorySearch] = useState("");
  const [categoryOpen, setCategoryOpen] = useState(false);

  const form = useForm<VendorTypeFormData>({
    resolver: zodResolver(vendorTypeFormSchema),
    defaultValues: {
      typeName: '',
      vendorCategoryId: 0,
      description: '',
    },
  });

  // Fetch vendor categories using search hook
  const { data: categories = [], isLoading: isLoadingCategories } = vendorCategoriesApi.useSearch(categorySearch);

  // Fetch initial categories for display
  const { data: allCategories = [] } = vendorCategoriesApi.useSearch("");

  // Combine search results with selected category
  const displayCategories = (() => {
    if (!currentRow?.vendorCategory?.id) return categories;
    const selected = allCategories.find(c => c.id === currentRow.vendorCategory.id);
    if (!selected) return categories;
    if (categories.some(c => c.id === selected.id)) return categories;
    return [selected, ...categories];
  })();

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        typeName: currentRow.typeName,
        vendorCategoryId: currentRow.vendorCategory?.id || 0,
        description: currentRow.description || '',
      });
    } else {
      form.reset({
        typeName: '',
        vendorCategoryId: 0,
        description: '',
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: vendorTypesApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vendor-types'] });
      toast.success('Vendor type created successfully');
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: VendorTypeFormData }) =>
      vendorTypesApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vendor-types'] });
      toast.success('Vendor type updated successfully');
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: VendorTypeFormData) => {
    if (isUpdate && currentRow) {
      updateMutation.mutate({ id: currentRow.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>{isUpdate ? 'Update' : 'Create'} Vendor Type</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? 'Update the vendor type by providing necessary info.'
              : 'Add a new vendor type by providing necessary info.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="vendor-types-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="typeName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Type Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter vendor type name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="vendorCategoryId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vendor Category *</FormLabel>
                  <Popover open={categoryOpen} onOpenChange={setCategoryOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={categoryOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? categories.find((c) => c.id === field.value)?.categoryName || "Select category"
                            : "Select a vendor category"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search vendor categories..."
                          value={categorySearch}
                          onValueChange={setCategorySearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isLoadingCategories ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No vendor category found."
                            )}
                          </CommandEmpty>
                          {categories.map((category) => (
                            <CommandItem
                              key={category.id}
                              value={String(category.id)}
                              onSelect={() => {
                                field.onChange(category.id);
                                setCategoryOpen(false);
                                setCategorySearch("");
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  field.value === category.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {category.categoryName}
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
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter description"
                      className="resize-none"
                      rows={4}
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>
        <SheetFooter className="mt-4 gap-2 px-4 sm:space-x-0">
          <SheetClose asChild>
            <Button
              type="button"
              variant="outline"
              disabled={createMutation.isPending || updateMutation.isPending}
            >
              Cancel
            </Button>
          </SheetClose>
          <Button
            type="submit"
            form="vendor-types-form"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {createMutation.isPending || updateMutation.isPending ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Saving...
              </>
            ) : isUpdate ? (
              'Update'
            ) : (
              'Create'
            )}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
