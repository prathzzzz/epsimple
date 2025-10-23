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
import { useCostTypes } from "../context/cost-types-provider";
import { costTypesApi } from "../api/cost-types-api";
import { costTypeSchema, type CostTypeFormData } from "../api/schema";
import { getAllCostCategoriesList } from "@/features/cost-categories/api/cost-categories-api";

export function CostTypeDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, selectedCostType, setSelectedCostType, isEditMode, setIsEditMode } =
    useCostTypes();

  const createMutation = costTypesApi.useCreate();
  const updateMutation = costTypesApi.useUpdate();

  const { data: costCategoriesResponse } = useQuery({
    queryKey: ["cost-categories", "list"],
    queryFn: getAllCostCategoriesList,
  });

  const costCategories = costCategoriesResponse?.data || [];

  const form = useForm<CostTypeFormData>({
    resolver: zodResolver(costTypeSchema),
    defaultValues: {
      typeName: "",
      typeDescription: "",
      costCategoryId: 0,
    },
  });

  useEffect(() => {
    if (isEditMode && selectedCostType) {
      form.reset({
        typeName: selectedCostType.typeName,
        typeDescription: selectedCostType.typeDescription || "",
        costCategoryId: selectedCostType.costCategoryId,
      });
    } else {
      form.reset({
        typeName: "",
        typeDescription: "",
        costCategoryId: 0,
      });
    }
  }, [selectedCostType, isEditMode, form]);

  const onSubmit = async (data: CostTypeFormData) => {
    if (isEditMode && selectedCostType) {
      updateMutation.mutate(
        {
          id: selectedCostType.id,
          data,
        },
        {
          onSuccess: () => {
            handleClose();
          },
        }
      );
    } else {
      createMutation.mutate(data, {
        onSuccess: () => {
          handleClose();
        },
      });
    }
  };

  const handleClose = () => {
    setIsDrawerOpen(false);
    setSelectedCostType(null);
    setIsEditMode(false);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>
            {isEditMode ? "Update" : "Create"} Cost Type
          </SheetTitle>
          <SheetDescription>
            {isEditMode
              ? "Update the cost type by providing necessary info."
              : "Add a new cost type by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="cost-type-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="costCategoryId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cost Category *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ""}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a cost category" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {costCategories.map((category) => (
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
              name="typeDescription"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Type Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter type description (optional)"
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

        <SheetFooter className="gap-2 pt-4">
          <SheetClose asChild>
            <Button type="button" variant="outline" onClick={handleClose}>
              Cancel
            </Button>
          </SheetClose>
          <Button
            type="submit"
            form="cost-type-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {isEditMode ? "Update" : "Create"}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
