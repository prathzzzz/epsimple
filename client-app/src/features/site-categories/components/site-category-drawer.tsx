import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";
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
import { useSiteCategoryContext } from "../context/site-category-provider";
import { siteCategoryApi } from "../api/site-category-api";
import { siteCategoryFormSchema, type SiteCategoryFormData } from "../api/schema";

export function SiteCategoryDrawer() {
  const {
    isDrawerOpen,
    setIsDrawerOpen,
    editingCategory,
    setEditingCategory,
  } = useSiteCategoryContext();

  const form = useForm<SiteCategoryFormData>({
    resolver: zodResolver(siteCategoryFormSchema),
    defaultValues: {
      categoryName: "",
      categoryCode: "",
      description: "",
    },
  });

  const createMutation = siteCategoryApi.useCreate();
  const updateMutation = siteCategoryApi.useUpdate();

  useEffect(() => {
    if (editingCategory) {
      form.reset({
        categoryName: editingCategory.categoryName,
        categoryCode: editingCategory.categoryCode || "",
        description: editingCategory.description || "",
      });
    } else {
      form.reset({
        categoryName: "",
        categoryCode: "",
        description: "",
      });
    }
  }, [editingCategory, form]);

  const onSubmit = (data: SiteCategoryFormData) => {
    if (editingCategory) {
      updateMutation.mutate(
        { id: editingCategory.id, data },
        {
          onSuccess: () => {
            setIsDrawerOpen(false);
            setEditingCategory(null);
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
    setEditingCategory(null);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>
            {editingCategory ? "Update" : "Create"} Site Category
          </SheetTitle>
          <SheetDescription>
            {editingCategory
              ? "Update the site category by providing necessary info."
              : "Add a new site category by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="site-category-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="categoryName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Category Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="e.g., Manufacturing" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="categoryCode"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Category Code</FormLabel>
                  <FormControl>
                    <Input 
                      placeholder="e.g., FACTORY, WAREHOUSE" 
                      {...field}
                      className="font-mono"
                    />
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
              disabled={isLoading}
            >
              Cancel
            </Button>
          </SheetClose>
          <Button
            type="submit"
            form="site-category-form"
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Saving...
              </>
            ) : editingCategory ? (
              "Update"
            ) : (
              "Create"
            )}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
