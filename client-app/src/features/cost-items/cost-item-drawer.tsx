import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';
import { useCostItemContext } from './cost-item-provider';
import { costItemSchema, type CostItemFormValues } from './schema';
import { useCreateCostItem, useUpdateCostItem } from '@/features/cost-items/api/cost-items-api';
import { costTypesApi } from '@/features/cost-types/api/cost-types-api';
import { cn } from '@/lib/utils';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
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

export const CostItemDrawer = () => {
  const { isDrawerOpen, closeDrawer, editingCostItem } = useCostItemContext();
  const [costTypeSearch, setCostTypeSearch] = useState('');
  const [costTypeOpen, setCostTypeOpen] = useState(false);

  const { data: costTypes = [], isLoading: costTypesLoading } = costTypesApi.useSearch(costTypeSearch);
  
  // Fetch initial cost types to ensure selected type is displayed when editing
  const { data: allCostTypes = [] } = costTypesApi.useSearch('');
  
  // Combine search results with selected cost type
  const displayCostTypes = (() => {
    if (!editingCostItem?.costTypeId) return costTypes;
    const selectedType = allCostTypes.find(t => t.id === editingCostItem.costTypeId);
    if (!selectedType) return costTypes;
    // Check if selected type is already in the list
    if (costTypes.some(t => t.id === selectedType.id)) return costTypes;
    // Add selected type to the top of the list
    return [selectedType, ...costTypes];
  })();
  
  const createMutation = useCreateCostItem();
  const updateMutation = useUpdateCostItem();

  const form = useForm<CostItemFormValues>({
    resolver: zodResolver(costItemSchema),
    defaultValues: {
      costTypeId: 0,
      costItemFor: '',
      itemDescription: '',
    },
  });

  useEffect(() => {
    if (editingCostItem) {
      form.reset({
        costTypeId: editingCostItem.costTypeId,
        costItemFor: editingCostItem.costItemFor,
        itemDescription: editingCostItem.itemDescription || '',
      });
    } else {
      form.reset({
        costTypeId: 0,
        costItemFor: '',
        itemDescription: '',
      });
    }
  }, [editingCostItem, form]);

  const onSubmit = async (data: CostItemFormValues) => {
    try {
      if (editingCostItem) {
        await updateMutation.mutateAsync({
          id: editingCostItem.id,
          data,
        });
      } else {
        await createMutation.mutateAsync(data);
      }
      closeDrawer();
      form.reset();
    } catch (_error) {
      // Error is handled by mutation
    }
  };

  return (
    <Sheet open={isDrawerOpen} onOpenChange={closeDrawer}>
      <SheetContent className="flex flex-col sm:max-w-[425px]">
        <SheetHeader>
          <SheetTitle>
            {editingCostItem ? 'Edit Cost Item' : 'Create Cost Item'}
          </SheetTitle>
          <SheetDescription>
            {editingCostItem
              ? 'Update the cost item details'
              : 'Add a new cost item to the system'}
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form 
            id="cost-item-form"
            onSubmit={form.handleSubmit(onSubmit)} 
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="costTypeId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Cost Type *</FormLabel>
                  <Popover open={costTypeOpen} onOpenChange={setCostTypeOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={costTypeOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayCostTypes.find((t) => t.id === field.value)?.typeName || "Select cost type"
                            : "Select cost type"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search cost types..."
                          value={costTypeSearch}
                          onValueChange={setCostTypeSearch}
                        />
                        <CommandList>
                          {costTypesLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayCostTypes.length === 0 ? (
                            <CommandEmpty>No cost type found.</CommandEmpty>
                          ) : (
                            displayCostTypes.map((type) => (
                              <CommandItem
                                key={type.id}
                                value={String(type.id)}
                                onSelect={() => {
                                  field.onChange(type.id);
                                  setCostTypeOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    type.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {type.typeName}
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
              name="costItemFor"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cost Item For *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Enter cost item for"
                      {...field}
                      maxLength={255}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="itemDescription"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter description (optional)"
                      {...field}
                      maxLength={1000}
                      rows={4}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 gap-2 px-4 sm:space-x-0">
          <Button
            type="button"
            variant="outline"
            onClick={closeDrawer}
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            form="cost-item-form"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {(createMutation.isPending || updateMutation.isPending) && (
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            )}
            {editingCostItem ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
};
