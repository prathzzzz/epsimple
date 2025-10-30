import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2 } from "lucide-react";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";
import { useAssetTagCodeGeneratorContext } from "../context/asset-tag-generator-provider";
import { assetTagCodeGeneratorApi } from "../api/asset-tag-generator-api";
import { assetTagCodeGeneratorFormSchema, type AssetTagCodeGeneratorFormData } from "../api/schema";
import { assetCategoryApi } from "@/features/asset-categories/api/asset-categories-api";
import { useVendorsList } from "@/lib/vendors-api";
import { getAllBanksList } from "@/lib/banks-api";

export function AssetTagCodeGeneratorDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingGenerator, setEditingGenerator } = useAssetTagCodeGeneratorContext();

  const createMutation = assetTagCodeGeneratorApi.useCreate();
  const updateMutation = assetTagCodeGeneratorApi.useUpdate();

  const { data: assetCategoriesResponse } = useQuery({
    queryKey: ["asset-categories", "list"],
    queryFn: () => assetCategoryApi.getList(),
  });

  const { data: vendorsData } = useVendorsList();
  
  const { data: banksData } = useQuery({
    queryKey: ["banks", "list"],
    queryFn: () => getAllBanksList(),
  });

  const assetCategories = assetCategoriesResponse || [];
  const vendors = vendorsData || [];
  const banks = banksData?.data || [];

  const form = useForm<AssetTagCodeGeneratorFormData>({
    resolver: zodResolver(assetTagCodeGeneratorFormSchema),
    defaultValues: {
      assetCategoryId: 0,
      vendorId: 0,
      bankId: 0,
      maxSeqDigit: 5,
      runningSeq: 1,
    },
  });

  useEffect(() => {
    if (editingGenerator) {
      form.reset({
        assetCategoryId: editingGenerator.assetCategoryId,
        vendorId: editingGenerator.vendorId,
        bankId: editingGenerator.bankId,
        maxSeqDigit: editingGenerator.maxSeqDigit,
        runningSeq: editingGenerator.runningSeq,
      });
    } else {
      form.reset({
        assetCategoryId: 0,
        vendorId: 0,
        bankId: 0,
        maxSeqDigit: 5,
        runningSeq: 1,
      });
    }
  }, [editingGenerator, form]);

  const onSubmit = async (data: AssetTagCodeGeneratorFormData) => {
    try {
      if (editingGenerator) {
        await updateMutation.mutateAsync({ id: editingGenerator.id, data });
      } else {
        await createMutation.mutateAsync(data);
      }
      handleClose();
    } catch (error) {
      // Error is handled by the mutation
    }
  };

  const handleClose = () => {
    setIsDrawerOpen(false);
    setEditingGenerator(null);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col sm:max-w-2xl">
        <SheetHeader>
          <SheetTitle>{editingGenerator ? "Edit" : "Create"} Asset Tag Generator</SheetTitle>
          <SheetDescription>
            {editingGenerator
              ? "Update the asset tag generator configuration"
              : "Add a new asset tag generator configuration"}
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="flex-1 space-y-6 overflow-y-auto px-4 py-6">
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="assetCategoryId"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Asset Category *</FormLabel>
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ""}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select asset category" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {assetCategories.map((category) => (
                          <SelectItem key={category.id} value={String(category.id)}>
                            {category.categoryName}
                            {category.categoryCode && ` (${category.categoryCode})`}
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
                  <FormItem className="flex-1">
                    <FormLabel>Vendor *</FormLabel>
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ""}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select vendor" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {vendors.map((vendor) => (
                          <SelectItem key={vendor.id} value={String(vendor.id)}>
                            {vendor.vendorName}
                            {vendor.vendorCodeAlt && ` (${vendor.vendorCodeAlt})`}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="bankId"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Bank *</FormLabel>
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ""}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select bank" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {banks.map((bank) => (
                          <SelectItem key={bank.id} value={String(bank.id)}>
                            {bank.bankName} ({bank.bankCodeAlt})
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
                name="maxSeqDigit"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Max Sequence Digits (1-10)</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        placeholder="Default: 5"
                        min={1}
                        max={10}
                        {...field}
                        onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : 5)}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <FormField
              control={form.control}
              name="runningSeq"
              render={({ field }) => (
                <FormItem className="flex-1">
                  <FormLabel>Running Sequence</FormLabel>
                  <FormControl>
                    <Input
                      type="number"
                      placeholder="Default: 1"
                      min={1}
                      {...field}
                      onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : 1)}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <SheetFooter className="px-4">
              <SheetClose asChild>
                <Button variant="outline" type="button">
                  Cancel
                </Button>
              </SheetClose>
              <Button type="submit" disabled={isLoading}>
                {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                {editingGenerator ? "Update" : "Create"}
              </Button>
            </SheetFooter>
          </form>
        </Form>
      </SheetContent>
    </Sheet>
  );
}
