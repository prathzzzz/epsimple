import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
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
import { Loader2 } from "lucide-react";
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
import { siteTypeApi } from "../api/site-type-api";
import {
  siteTypeFormSchema,
  type SiteTypeFormData,
  type SiteType,
} from "../api/schema";

interface SiteTypeMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: SiteType | null;
}

export function SiteTypeMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: SiteTypeMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const form = useForm<SiteTypeFormData>({
    resolver: zodResolver(siteTypeFormSchema),
    defaultValues: {
      typeName: "",
      description: "",
    },
  });

  useEffect(() => {
    if (currentRow) {
      form.reset({
        typeName: currentRow.typeName,
        description: currentRow.description || "",
      });
    } else {
      form.reset({
        typeName: "",
        description: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: siteTypeApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["site-types"] });
      toast.success("Site type created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: SiteTypeFormData }) =>
      siteTypeApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["site-types"] });
      toast.success("Site type updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: SiteTypeFormData) => {
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
          <SheetTitle>{isUpdate ? "Update" : "Create"} Site Type</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the site type by providing necessary info."
              : "Add a new site type by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="site-type-form"
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
                    <Input placeholder="e.g., Telecom Tower" {...field} />
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
            form="site-type-form"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {createMutation.isPending || updateMutation.isPending ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Saving...
              </>
            ) : isUpdate ? (
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
