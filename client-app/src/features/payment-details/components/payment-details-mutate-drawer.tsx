import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";
import { format } from "date-fns";

import { cn } from "@/lib/utils";
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
import { DatePicker } from "@/components/date-picker";

import { paymentDetailsApi } from "../api/payment-details-api";
import { paymentMethodsApi } from "@/features/payment-methods/api/payment-methods-api";
import {
  paymentDetailsFormSchema,
  type PaymentDetailsFormData,
  type PaymentDetails,
} from "../api/schema";

interface PaymentDetailsMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: PaymentDetails | null;
}

export function PaymentDetailsMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: PaymentDetailsMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const [paymentMethodSearch, setPaymentMethodSearch] = useState("");
  const [paymentMethodOpen, setPaymentMethodOpen] = useState(false);

  // Fetch payment methods with search
  const { data: paymentMethods = [], isLoading: isPaymentMethodsLoading } =
    paymentMethodsApi.useSearch(paymentMethodSearch);
  
  // Fetch initial payment methods to ensure selected method is displayed when editing
  const { data: allPaymentMethods = [] } = paymentMethodsApi.useSearch("");
  
  // Combine search results with selected payment method
  const displayPaymentMethods = (() => {
    if (!currentRow?.paymentMethodId) return paymentMethods;
    const selectedMethod = allPaymentMethods.find(m => m.id === currentRow.paymentMethodId);
    if (!selectedMethod) return paymentMethods;
    // Check if selected method is already in the list
    if (paymentMethods.some(m => m.id === selectedMethod.id)) return paymentMethods;
    // Add selected method to the top of the list
    return [selectedMethod, ...paymentMethods];
  })();

  const form = useForm<PaymentDetailsFormData>({
    resolver: zodResolver(paymentDetailsFormSchema),
    defaultValues: {
      paymentMethodId: 0,
      paymentDate: "",
      paymentAmount: 0,
      transactionNumber: "",
      vpa: "",
      beneficiaryName: "",
      beneficiaryAccountNumber: "",
      paymentRemarks: "",
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        paymentMethodId: currentRow.paymentMethodId,
        paymentDate: currentRow.paymentDate,
        paymentAmount: currentRow.paymentAmount,
        transactionNumber: currentRow.transactionNumber || "",
        vpa: currentRow.vpa || "",
        beneficiaryName: currentRow.beneficiaryName || "",
        beneficiaryAccountNumber: currentRow.beneficiaryAccountNumber || "",
        paymentRemarks: currentRow.paymentRemarks || "",
      });
    } else {
      form.reset({
        paymentMethodId: 0,
        paymentDate: "",
        paymentAmount: 0,
        transactionNumber: "",
        vpa: "",
        beneficiaryName: "",
        beneficiaryAccountNumber: "",
        paymentRemarks: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: paymentDetailsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payment-details"] });
      toast.success("Payment details created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: PaymentDetailsFormData }) =>
      paymentDetailsApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payment-details"] });
      toast.success("Payment details updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: PaymentDetailsFormData) => {
    if (isUpdate && currentRow) {
      updateMutation.mutate({ id: currentRow.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent className="flex flex-col overflow-hidden sm:max-w-xl">
        <SheetHeader className="flex-shrink-0 text-start">
          <SheetTitle>
            {isUpdate ? "Update" : "Create"} Payment Details
          </SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the payment details by providing necessary info."
              : "Add new payment details by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="payment-details-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="paymentMethodId"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Payment Method *</FormLabel>
                    <Popover open={paymentMethodOpen} onOpenChange={setPaymentMethodOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            aria-expanded={paymentMethodOpen}
                            className={cn(
                              "justify-between font-normal",
                              !field.value && "text-muted-foreground"
                            )}
                          >
                            {field.value
                              ? displayPaymentMethods.find((m) => m.id === field.value)?.methodName || "Select payment method"
                              : "Select payment method"}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search payment methods..."
                            value={paymentMethodSearch}
                            onValueChange={setPaymentMethodSearch}
                          />
                          <CommandList>
                            {isPaymentMethodsLoading ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : displayPaymentMethods.length === 0 ? (
                              <CommandEmpty>No payment method found.</CommandEmpty>
                            ) : (
                              displayPaymentMethods.map((method) => (
                                <CommandItem
                                  key={method.id}
                                  value={String(method.id)}
                                  onSelect={() => {
                                    field.onChange(method.id);
                                    setPaymentMethodOpen(false);
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      "mr-2 h-4 w-4",
                                      method.id === field.value ? "opacity-100" : "opacity-0"
                                    )}
                                  />
                                  {method.methodName}
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
                name="paymentDate"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Payment Date *</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                      }
                      placeholder="Select payment date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <FormField
              control={form.control}
              name="paymentAmount"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Payment Amount *</FormLabel>
                  <FormControl>
                    <Input
                      type="number"
                      step="0.01"
                      placeholder="Enter payment amount"
                      {...field}
                      onChange={(e) => field.onChange(parseFloat(e.target.value) || 0)}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="transactionNumber"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Transaction Number</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter transaction number" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="vpa"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>VPA</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter VPA" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="beneficiaryName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Beneficiary Name</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter beneficiary name" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="beneficiaryAccountNumber"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Account Number</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter account number" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <FormField
              control={form.control}
              name="paymentRemarks"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Remarks</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter payment remarks"
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
        <SheetFooter className="flex-shrink-0 mt-4 gap-2 px-4 sm:space-x-0">
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
            form="payment-details-form"
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
