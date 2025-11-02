import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import { cn } from "@/lib/utils";
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
import { useSearchVendors } from "@/lib/vendors-api";
import { useSearchBanks } from "@/lib/banks-api";

export function AssetTagCodeGeneratorDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingGenerator, setEditingGenerator } = useAssetTagCodeGeneratorContext();

  const [assetCategorySearch, setAssetCategorySearch] = useState("");
  const [assetCategoryOpen, setAssetCategoryOpen] = useState(false);
  const [vendorSearch, setVendorSearch] = useState("");
  const [vendorOpen, setVendorOpen] = useState(false);
  const [bankSearch, setBankSearch] = useState("");
  const [bankOpen, setBankOpen] = useState(false);

  const createMutation = assetTagCodeGeneratorApi.useCreate();
  const updateMutation = assetTagCodeGeneratorApi.useUpdate();

  const { data: assetCategories = [], isLoading: isLoadingAssetCategories } = 
    assetCategoryApi.useSearch(assetCategorySearch);
  const { data: vendors = [], isLoading: isLoadingVendors } = 
    useSearchVendors(vendorSearch);
  const { data: banks = [], isLoading: isLoadingBanks } = 
    useSearchBanks(bankSearch);

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
                    <Popover open={assetCategoryOpen} onOpenChange={setAssetCategoryOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              "w-full justify-between",
                              !field.value && "text-muted-foreground"
                            )}
                          >
                            {field.value
                              ? assetCategories.find((c) => c.id === field.value)?.categoryName +
                                (assetCategories.find((c) => c.id === field.value)?.categoryCode
                                  ? ` (${assetCategories.find((c) => c.id === field.value)?.categoryCode})`
                                  : "")
                              : "Select asset category"}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search asset categories..."
                            value={assetCategorySearch}
                            onValueChange={setAssetCategorySearch}
                          />
                          <CommandList>
                            {isLoadingAssetCategories ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : assetCategories.length === 0 ? (
                              <CommandEmpty>No asset categories found.</CommandEmpty>
                            ) : (
                              <CommandGroup>
                                {assetCategories.map((category) => (
                                  <CommandItem
                                    key={category.id}
                                    value={String(category.id)}
                                    onSelect={() => {
                                      field.onChange(category.id);
                                      setAssetCategoryOpen(false);
                                      setAssetCategorySearch("");
                                    }}
                                  >
                                    <Check
                                      className={cn(
                                        "mr-2 h-4 w-4",
                                        category.id === field.value ? "opacity-100" : "opacity-0"
                                      )}
                                    />
                                    {category.categoryName}
                                    {category.categoryCode && ` (${category.categoryCode})`}
                                  </CommandItem>
                                ))}
                              </CommandGroup>
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
                  <FormItem className="flex-1">
                    <FormLabel>Vendor *</FormLabel>
                    <Popover open={vendorOpen} onOpenChange={setVendorOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              "w-full justify-between",
                              !field.value && "text-muted-foreground"
                            )}
                          >
                            {field.value
                              ? vendors.find((v) => v.id === field.value)?.vendorName +
                                (vendors.find((v) => v.id === field.value)?.vendorCodeAlt
                                  ? ` (${vendors.find((v) => v.id === field.value)?.vendorCodeAlt})`
                                  : "")
                              : "Select vendor"}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search vendors..."
                            value={vendorSearch}
                            onValueChange={setVendorSearch}
                          />
                          <CommandList>
                            {isLoadingVendors ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : vendors.length === 0 ? (
                              <CommandEmpty>No vendors found.</CommandEmpty>
                            ) : (
                              <CommandGroup>
                                {vendors.map((vendor) => (
                                  <CommandItem
                                    key={vendor.id}
                                    value={String(vendor.id)}
                                    onSelect={() => {
                                      field.onChange(vendor.id);
                                      setVendorOpen(false);
                                      setVendorSearch("");
                                    }}
                                  >
                                    <Check
                                      className={cn(
                                        "mr-2 h-4 w-4",
                                        vendor.id === field.value ? "opacity-100" : "opacity-0"
                                      )}
                                    />
                                    {vendor.vendorName}
                                    {vendor.vendorCodeAlt && ` (${vendor.vendorCodeAlt})`}
                                  </CommandItem>
                                ))}
                              </CommandGroup>
                            )}
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
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
                    <Popover open={bankOpen} onOpenChange={setBankOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              "w-full justify-between",
                              !field.value && "text-muted-foreground"
                            )}
                          >
                            {field.value
                              ? `${banks.find((b) => b.id === field.value)?.bankName} (${banks.find((b) => b.id === field.value)?.bankCodeAlt})`
                              : "Select bank"}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search banks..."
                            value={bankSearch}
                            onValueChange={setBankSearch}
                          />
                          <CommandList>
                            {isLoadingBanks ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : banks.length === 0 ? (
                              <CommandEmpty>No banks found.</CommandEmpty>
                            ) : (
                              <CommandGroup>
                                {banks.map((bank) => (
                                  <CommandItem
                                    key={bank.id}
                                    value={String(bank.id)}
                                    onSelect={() => {
                                      field.onChange(bank.id);
                                      setBankOpen(false);
                                      setBankSearch("");
                                    }}
                                  >
                                    <Check
                                      className={cn(
                                        "mr-2 h-4 w-4",
                                        bank.id === field.value ? "opacity-100" : "opacity-0"
                                      )}
                                    />
                                    {bank.bankName} ({bank.bankCodeAlt})
                                  </CommandItem>
                                ))}
                              </CommandGroup>
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
