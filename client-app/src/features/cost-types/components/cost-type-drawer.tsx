import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Command,
  CommandEmpty,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
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
import { costCategoriesApi } from "@/features/cost-categories/api/cost-categories-api";

export function CostTypeDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, selectedCostType, setSelectedCostType, isEditMode, setIsEditMode } =
    useCostTypes();

  const [costCategorySearch, setCostCategorySearch] = useState("");
  const [costCategoryOpen, setCostCategoryOpen] = useState(false);

  const createMutation = costTypesApi.useCreate();
  const updateMutation = costTypesApi.useUpdate();

  const { data: costCategories = [], isLoading: isCostCategoriesLoading } =
    costCategoriesApi.useSearch(costCategorySearch);
  
  // Fetch initial cost categories to ensure selected category is displayed when editing
  const { data: allCostCategories = [] } = costCategoriesApi.useSearch("");
  
  // Combine search results with selected cost category
  const displayCostCategories = (() => {
    if (!selectedCostType?.costCategoryId) return costCategories;
    const selectedCategory = allCostCategories.find(c => c.id === selectedCostType.costCategoryId);
    if (!selectedCategory) return costCategories;
    // Check if selected category is already in the list
    if (costCategories.some(c => c.id === selectedCategory.id)) return costCategories;
    // Add selected category to the top of the list
    return [selectedCategory, ...costCategories];
  })();

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
                <FormItem className="flex flex-col">
                  <FormLabel>Cost Category *</FormLabel>
                  <Popover open={costCategoryOpen} onOpenChange={setCostCategoryOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={costCategoryOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayCostCategories.find((c) => c.id === field.value)
                                ?.categoryName || "Select cost category"
                            : "Select cost category"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search cost categories..."
                          value={costCategorySearch}
                          onValueChange={setCostCategorySearch}
                        />
                        <CommandList>
                          {isCostCategoriesLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayCostCategories.length === 0 ? (
                            <CommandEmpty>No cost category found.</CommandEmpty>
                          ) : (
                            displayCostCategories.map((category) => (
                              <CommandItem
                                key={category.id}
                                value={String(category.id)}
                                onSelect={() => {
                                  field.onChange(category.id);
                                  setCostCategoryOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    category.id === field.value
                                      ? "opacity-100"
                                      : "opacity-0"
                                  )}
                                />
                                {category.categoryName}
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
