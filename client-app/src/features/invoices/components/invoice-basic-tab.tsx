import type { UseFormReturn } from "react-hook-form";
import { format } from "date-fns";
import { useState } from "react";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { DatePicker } from "@/components/date-picker";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Button } from "@/components/ui/button";
import { Command, CommandInput, CommandList, CommandEmpty, CommandItem, CommandGroup } from "@/components/ui/command";
import { cn } from "@/lib/utils";
import type { InvoiceFormData, Invoice } from "../api/schema";
import { genericStatusTypeApi } from "@/features/generic-status-types/api/generic-status-type-api";
import { payeeApi } from "@/features/payees/api/payee-api";
import { paymentDetailsApi } from "@/features/payment-details/api/payment-details-api";

interface InvoiceBasicTabProps {
  readonly form: UseFormReturn<InvoiceFormData>;
  readonly currentRow: Invoice | null;
}

export function InvoiceBasicTab({ form, currentRow }: InvoiceBasicTabProps) {
  const [payeeSearch, setPayeeSearch] = useState("");
  const [payeeOpen, setPayeeOpen] = useState(false);
  const [paymentDetailsSearch, setPaymentDetailsSearch] = useState("");
  const [paymentDetailsOpen, setPaymentDetailsOpen] = useState(false);
  const [statusSearch, setStatusSearch] = useState("");
  const [statusOpen, setStatusOpen] = useState(false);

  const { data: payees = [], isLoading: isLoadingPayees } = payeeApi.useSearch({
    searchTerm: payeeSearch,
    page: 0,
    size: 50,
  });
  
  const { data: paymentDetailsList = [], isLoading: isLoadingPaymentDetails } = paymentDetailsApi.useSearch(paymentDetailsSearch);
  
  const { data: paymentStatuses = [], isLoading: isLoadingStatuses } = 
    genericStatusTypeApi.useSearch(statusSearch);

  // Fetch initial items for display
  const { data: allPayees = [] } = payeeApi.useSearch({
    searchTerm: "",
    page: 0,
    size: 20,
  });
  const { data: allPaymentDetails = [] } = paymentDetailsApi.useSearch("");
  const { data: allPaymentStatuses = [] } = genericStatusTypeApi.useSearch("");

  // Combine search results with selected items
  const displayPayees = (() => {
    if (!currentRow?.payeeId) return payees;
    const selected = allPayees.find(p => p.id === currentRow.payeeId);
    if (!selected) return payees;
    if (payees.some(p => p.id === selected.id)) return payees;
    return [selected, ...payees];
  })();

  const displayPaymentDetails = (() => {
    if (!currentRow?.paymentDetailsId) return paymentDetailsList;
    const selected = allPaymentDetails.find(pd => pd.id === currentRow.paymentDetailsId);
    if (!selected) return paymentDetailsList;
    if (paymentDetailsList.some(pd => pd.id === selected.id)) return paymentDetailsList;
    return [selected, ...paymentDetailsList];
  })();

  const displayPaymentStatuses = (() => {
    if (!currentRow?.paymentStatus) return paymentStatuses;
    const selected = allPaymentStatuses.find(s => s.statusName === currentRow.paymentStatus);
    if (!selected) return paymentStatuses;
    if (paymentStatuses.some(s => s.id === selected.id)) return paymentStatuses;
    return [selected, ...paymentStatuses];
  })();

  return (
    <div className="space-y-4 mt-4">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="invoiceNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Invoice Number *</FormLabel>
              <FormControl>
                <Input placeholder="Enter invoice number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="invoiceDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Invoice Date *</FormLabel>
              <DatePicker
                selected={field.value ? new Date(field.value) : undefined}
                onSelect={(date) =>
                  field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                }
              />
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="payeeId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Payee *</FormLabel>
              <Popover open={payeeOpen} onOpenChange={setPayeeOpen}>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant="outline"
                      role="combobox"
                      aria-expanded={payeeOpen}
                      className={cn(
                        "w-full justify-between",
                        !field.value && "text-muted-foreground"
                      )}
                    >
                      {field.value > 0
                        ? displayPayees.find((p) => p.id === field.value)?.payeeName || "Select payee"
                        : "Select payee"}
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                  <Command shouldFilter={false}>
                    <CommandInput
                      placeholder="Search payees..."
                      value={payeeSearch}
                      onValueChange={setPayeeSearch}
                    />
                    <CommandList>
                      <CommandEmpty>
                        {isLoadingPayees ? (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        ) : (
                          "No payee found."
                        )}
                      </CommandEmpty>
                      {displayPayees.map((payee) => (
                        <CommandItem
                          key={payee.id}
                          value={String(payee.id)}
                          onSelect={() => {
                            field.onChange(payee.id);
                            setPayeeOpen(false);
                            setPayeeSearch("");
                          }}
                        >
                          <Check
                            className={cn(
                              "mr-2 h-4 w-4",
                              field.value === payee.id ? "opacity-100" : "opacity-0"
                            )}
                          />
                          {payee.payeeName} ({payee.payeeTypeName})
                        </CommandItem>
                      ))}
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
          name="paymentDetailsId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Payment Details</FormLabel>
              <Popover open={paymentDetailsOpen} onOpenChange={setPaymentDetailsOpen}>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant="outline"
                      role="combobox"
                      aria-expanded={paymentDetailsOpen}
                      className={cn(
                        "w-full justify-between",
                        !field.value && "text-muted-foreground"
                      )}
                    >
                      {field.value
                        ? displayPaymentDetails.find((p) => p.id === field.value)?.transactionNumber || `Payment ${field.value}`
                        : "Select payment details (optional)"}
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                  <Command shouldFilter={false}>
                    <CommandInput
                      placeholder="Search payment details..."
                      value={paymentDetailsSearch}
                      onValueChange={setPaymentDetailsSearch}
                    />
                    <CommandList>
                      <CommandEmpty>
                        {isLoadingPaymentDetails ? (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        ) : (
                          "No payment details found."
                        )}
                      </CommandEmpty>
                      {displayPaymentDetails.map((payment) => (
                        <CommandItem
                          key={payment.id}
                          value={String(payment.id)}
                          onSelect={() => {
                            field.onChange(payment.id);
                            setPaymentDetailsOpen(false);
                            setPaymentDetailsSearch("");
                          }}
                        >
                          <Check
                            className={cn(
                              "mr-2 h-4 w-4",
                              field.value === payment.id ? "opacity-100" : "opacity-0"
                            )}
                          />
                          {payment.transactionNumber || `Payment ${payment.id}`} - {payment.paymentMethodName}
                        </CommandItem>
                      ))}
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
          name="vendorName"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Vendor Name</FormLabel>
              <FormControl>
                <Input placeholder="Enter vendor name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="orderNumber"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Order Number</FormLabel>
              <FormControl>
                <Input placeholder="Enter order number" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="paymentStatus"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Payment Status</FormLabel>
              <Popover open={statusOpen} onOpenChange={setStatusOpen}>
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
                        ? displayPaymentStatuses.find((s) => s.statusName === field.value)?.statusName
                        : "Select status"}
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-full p-0" align="start">
                  <Command shouldFilter={false}>
                    <CommandInput
                      placeholder="Search statuses..."
                      value={statusSearch}
                      onValueChange={setStatusSearch}
                    />
                    <CommandList>
                      {isLoadingStatuses ? (
                        <div className="flex items-center justify-center py-6">
                          <Loader2 className="h-4 w-4 animate-spin" />
                        </div>
                      ) : displayPaymentStatuses.length === 0 ? (
                        <CommandEmpty>No statuses found.</CommandEmpty>
                      ) : (
                        <CommandGroup>
                          {displayPaymentStatuses.map((status) => (
                            <CommandItem
                              key={status.id}
                              value={String(status.id)}
                              onSelect={() => {
                                field.onChange(status.statusName);
                                setStatusOpen(false);
                                setStatusSearch("");
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  status.statusName === field.value ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {status.statusName}
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
          name="invoiceReceivedDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Invoice Received Date</FormLabel>
              <DatePicker
                selected={field.value ? new Date(field.value) : undefined}
                onSelect={(date) =>
                  field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                }
              />
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="paymentDueDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Payment Due Date</FormLabel>
              <DatePicker
                selected={field.value ? new Date(field.value) : undefined}
                onSelect={(date) =>
                  field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                }
              />
              <FormMessage />
            </FormItem>
          )}
        />
      </div>
    </div>
  );
}
