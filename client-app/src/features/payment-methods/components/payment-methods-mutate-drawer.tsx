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

import { paymentMethodsApi } from "../api/payment-methods-api";
import {
  paymentMethodFormSchema,
  type PaymentMethodFormData,
  type PaymentMethod,
} from "../api/schema";

interface PaymentMethodsMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: PaymentMethod | null;
}

export function PaymentMethodsMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: PaymentMethodsMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const form = useForm<PaymentMethodFormData>({
    resolver: zodResolver(paymentMethodFormSchema),
    defaultValues: {
      methodName: "",
      description: "",
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        methodName: currentRow.methodName,
        description: currentRow.description || "",
      });
    } else {
      form.reset({
        methodName: "",
        description: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: paymentMethodsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payment-methods"] });
      toast.success("Payment method created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: PaymentMethodFormData }) =>
      paymentMethodsApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payment-methods"] });
      toast.success("Payment method updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: PaymentMethodFormData) => {
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
            {isUpdate ? "Update" : "Create"} Payment Method
          </SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the payment method by providing necessary info."
              : "Add a new payment method by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="payment-methods-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="methodName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Method Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter method name" {...field} />
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
            form="payment-methods-form"
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
