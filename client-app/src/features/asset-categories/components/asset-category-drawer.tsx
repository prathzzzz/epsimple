import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2 } from "lucide-react";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
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
import { useAssetCategoryContext } from "../context/asset-category-provider";
import { assetCategoryApi } from "../api/asset-categories-api";
import { assetCategoryFormSchema, type AssetCategoryFormData } from "../api/schema";
import { assetTypesApi } from "@/features/asset-types/api/asset-types-api";
import type { AssetType } from "@/features/asset-types/api/schema";

export function AssetCategoryDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingAssetCategory, setEditingAssetCategory } =
    useAssetCategoryContext();

  const createMutation = assetCategoryApi.useCreate();
  const updateMutation = assetCategoryApi.useUpdate();

  const { data: assetTypesResponse } = useQuery({
    queryKey: ["asset-types", "list"],
    queryFn: () => assetTypesApi.getList(),
  });

  const assetTypes: AssetType[] = assetTypesResponse || [];

  const form = useForm<AssetCategoryFormData>({
    resolver: zodResolver(assetCategoryFormSchema),
    defaultValues: {
      categoryName: "",
      categoryCode: "",
      assetTypeId: 0,
      assetCodeAlt: "",
      description: "",
    },
  });

  useEffect(() => {
    if (editingAssetCategory) {
      form.reset({
        categoryName: editingAssetCategory.categoryName,
        categoryCode: editingAssetCategory.categoryCode,
        assetTypeId: editingAssetCategory.assetTypeId,
        assetCodeAlt: editingAssetCategory.assetCodeAlt,
        description: editingAssetCategory.description || "",
      });
    } else {
      form.reset({
        categoryName: "",
        categoryCode: "",
        assetTypeId: 0,
        assetCodeAlt: "",
        description: "",
      });
    }
  }, [editingAssetCategory, form]);

  const onSubmit = async (data: AssetCategoryFormData) => {
    if (editingAssetCategory) {
      updateMutation.mutate(
        {
          id: editingAssetCategory.id,
          data,
        },
        {
          onSuccess: () => {
            setIsDrawerOpen(false);
            setEditingAssetCategory(null);
            form.reset();
          },
        }
      );
    } else {
      createMutation.mutate(data, {
        onSuccess: () => {
          setIsDrawerOpen(false);
          form.reset();
        },
      });
    }
  };

  const handleClose = () => {
    setIsDrawerOpen(false);
    setEditingAssetCategory(null);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>
            {editingAssetCategory ? "Update" : "Create"} Asset Category
          </SheetTitle>
          <SheetDescription>
            {editingAssetCategory
              ? "Update the asset category by providing necessary info."
              : "Add a new asset category by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="asset-category-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="assetTypeId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Asset Type *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ""}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select an asset type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {assetTypes.map((type: AssetType) => (
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
              name="categoryName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Category Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter category name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="categoryCode"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Category Code *</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="e.g., ATM, DESKTOP"
                        {...field}
                        className="font-mono uppercase"
                        onChange={(e) => field.onChange(e.target.value.toUpperCase())}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="assetCodeAlt"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Asset Code Alt *</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="e.g., ATM123"
                        {...field}
                        className="font-mono uppercase"
                        onChange={(e) => field.onChange(e.target.value.toUpperCase())}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter description"
                      className="min-h-[80px] resize-none"
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

        <SheetFooter className="flex-shrink-0 gap-2 px-4 sm:space-x-0">
          <SheetClose asChild>
            <Button variant="outline" disabled={isLoading}>
              Cancel
            </Button>
          </SheetClose>
          <Button type="submit" form="asset-category-form" disabled={isLoading}>
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {editingAssetCategory ? "Update" : "Save"}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
