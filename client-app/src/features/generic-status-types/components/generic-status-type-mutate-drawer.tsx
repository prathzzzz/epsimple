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

import { genericStatusTypeApi } from "../api/generic-status-type-api";
import {
  genericStatusTypeFormSchema,
  type GenericStatusTypeFormData,
  type GenericStatusType,
} from "../api/schema";

interface GenericStatusTypeMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: GenericStatusType | null;
}

export function GenericStatusTypeMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: GenericStatusTypeMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const form = useForm<GenericStatusTypeFormData>({
    resolver: zodResolver(genericStatusTypeFormSchema),
    defaultValues: {
      statusName: "",
      statusCode: "",
      description: "",
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        statusName: currentRow.statusName,
        statusCode: currentRow.statusCode || "",
        description: currentRow.description || "",
      });
    } else {
      form.reset({
        statusName: "",
        statusCode: "",
        description: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: genericStatusTypeApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["generic-status-types"] });
      toast.success("Generic status type created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number;
      data: GenericStatusTypeFormData;
    }) => genericStatusTypeApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["generic-status-types"] });
      toast.success("Generic status type updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: GenericStatusTypeFormData) => {
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
          <SheetTitle>
            {isUpdate ? "Update" : "Create"} Generic Status Type
          </SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the generic status type by providing necessary info."
              : "Add a new generic status type by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="generic-status-type-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="statusName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Status Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="e.g., Active" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="statusCode"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Status Code</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="e.g., ACTIVE"
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
              disabled={createMutation.isPending || updateMutation.isPending}
            >
              Cancel
            </Button>
          </SheetClose>
          <Button
            type="submit"
            form="generic-status-type-form"
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
