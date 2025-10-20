import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
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
import { Loader2 } from 'lucide-react';
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

  const form = useForm<VendorTypeFormData>({
    resolver: zodResolver(vendorTypeFormSchema),
    defaultValues: {
      typeName: '',
      vendorCategoryId: 0,
      description: '',
    },
  });

  // Fetch vendor categories list
  const { data: categoriesData } = useQuery({
    queryKey: ['vendor-categories-list'],
    queryFn: vendorCategoriesApi.getList,
  });

  const categories = categoriesData?.data || [];

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
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a vendor category" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {categories.map((category: any) => (
                        <SelectItem key={category.id} value={String(category.id)}>
                          {category.categoryName}
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
