import { useEffect } from 'react';
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

import { assetTypesApi } from '../api/asset-types-api';
import { assetTypeFormSchema, type AssetTypeFormData, type AssetType } from '../api/schema';

interface AssetTypesMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: AssetType | null;
}

export function AssetTypesMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: AssetTypesMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const form = useForm<AssetTypeFormData>({
    resolver: zodResolver(assetTypeFormSchema),
    defaultValues: {
      typeName: '',
      typeCode: '',
      description: '',
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        typeName: currentRow.typeName,
        typeCode: currentRow.typeCode,
        description: currentRow.description || '',
      });
    } else {
      form.reset({
        typeName: '',
        typeCode: '',
        description: '',
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: assetTypesApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['asset-types'] });
      toast.success('Asset type created successfully');
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: AssetTypeFormData }) =>
      assetTypesApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['asset-types'] });
      toast.success('Asset type updated successfully');
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: AssetTypeFormData) => {
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
          <SheetTitle>{isUpdate ? 'Update' : 'Create'} Asset Type</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? 'Update the asset type by providing necessary info.'
              : 'Add a new asset type by providing necessary info.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="asset-types-form"
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
                    <Input placeholder="Enter type name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="typeCode"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Type Code *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter type code" {...field} />
                  </FormControl>
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
            form="asset-types-form"
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
