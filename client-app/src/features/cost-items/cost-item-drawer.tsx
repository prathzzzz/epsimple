import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2 } from 'lucide-react';
import { useCostItemContext } from './cost-item-provider';
import { costItemSchema, type CostItemFormValues } from './schema';
import { useCreateCostItem, useUpdateCostItem } from '@/lib/cost-items-api';
import { costTypesApi } from '@/features/cost-types/api/cost-types-api';
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
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

export const CostItemDrawer = () => {
  const { isDrawerOpen, closeDrawer, editingCostItem } = useCostItemContext();
  const { data: costTypes, isLoading: costTypesLoading } = costTypesApi.useGetList();
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
    } catch (error) {
      // Error is handled by the mutation
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
                <FormItem>
                  <FormLabel>Cost Type *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                    disabled={costTypesLoading}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select cost type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {costTypes?.map((type) => (
                        <SelectItem key={type.id} value={String(type.id)}>
                          {type.typeName}
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
