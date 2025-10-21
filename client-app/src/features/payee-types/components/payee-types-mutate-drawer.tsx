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

import { payeeTypesApi } from "../api/payee-types-api";
import {
  payeeTypeFormSchema,
  type PayeeTypeFormData,
  type PayeeType,
} from "../api/schema";

interface PayeeTypesMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: PayeeType | null;
}

export function PayeeTypesMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: PayeeTypesMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const form = useForm<PayeeTypeFormData>({
    resolver: zodResolver(payeeTypeFormSchema),
    defaultValues: {
      payeeType: "",
      payeeCategory: "",
      description: "",
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        payeeType: currentRow.payeeType,
        payeeCategory: currentRow.payeeCategory || "",
        description: currentRow.description || "",
      });
    } else {
      form.reset({
        payeeType: "",
        payeeCategory: "",
        description: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: payeeTypesApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payee-types"] });
      toast.success("Payee type created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: PayeeTypeFormData }) =>
      payeeTypesApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payee-types"] });
      toast.success("Payee type updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: PayeeTypeFormData) => {
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
          <SheetTitle>{isUpdate ? "Update" : "Create"} Payee Type</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the payee type by providing necessary info."
              : "Add a new payee type by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="payee-types-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="payeeType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Payee Type *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter payee type" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="payeeCategory"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Payee Category</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter payee category" {...field} />
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
            form="payee-types-form"
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
