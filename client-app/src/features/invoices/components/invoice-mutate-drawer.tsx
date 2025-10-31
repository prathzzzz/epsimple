import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
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
import { Form } from "@/components/ui/form";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

import { invoicesApi } from "../api/invoices-api";
import { payeeApi } from "@/features/payees/api/payee-api";
import { genericStatusTypeApi } from "@/features/generic-status-types/api/generic-status-type-api";
import {
  invoiceFormSchema,
  type InvoiceFormData,
  type Invoice,
} from "../api/schema";
import { InvoiceBasicTab } from "./invoice-basic-tab";
import { InvoiceFinancialTab } from "./invoice-financial-tab";
import { InvoiceOtherTab } from "./invoice-other-tab";

interface InvoiceMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: Invoice | null;
}

export function InvoiceMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: InvoiceMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  // Fetch payees for dropdown
  const { data: payeesData } = useQuery({
    queryKey: ["payees", "list"],
    queryFn: () => payeeApi.getList(),
  });

  // Fetch payment statuses from generic status types
  const { data: statusesData } = useQuery({
    queryKey: ["generic-status-types", "list"],
    queryFn: () => genericStatusTypeApi.getList(),
  });

  const payees = payeesData?.data || [];
  const paymentStatuses = statusesData?.data || [];

  const form = useForm<InvoiceFormData>({
    resolver: zodResolver(invoiceFormSchema),
    defaultValues: {
      invoiceNumber: "",
      invoiceDate: "",
      invoiceReceivedDate: "",
      orderNumber: "",
      vendorName: "",
      payeeId: 0,
      paymentDetailsId: 0,
      paymentDueDate: "",
      paymentStatus: "",
      quantity: 0,
      unit: "",
      unitPrice: 0,
      taxCgstPercentage: 0,
      taxSgstPercentage: 0,
      taxIgstPercentage: 0,
      basicAmount: 0,
      cgst: 0,
      sgst: 0,
      igst: 0,
      totalAmount: 0,
      totalInvoiceValue: 0,
      netPayable: 0,
      remarks: "",
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        invoiceNumber: currentRow.invoiceNumber,
        invoiceDate: currentRow.invoiceDate,
        invoiceReceivedDate: currentRow.invoiceReceivedDate || "",
        orderNumber: currentRow.orderNumber || "",
        vendorName: currentRow.vendorName || "",
        payeeId: currentRow.payeeId,
        paymentDetailsId: currentRow.paymentDetailsId || 0,
        paymentDueDate: currentRow.paymentDueDate || "",
        paymentStatus: currentRow.paymentStatus || "",
        quantity: currentRow.quantity || 0,
        unit: currentRow.unit || "",
        unitPrice: currentRow.unitPrice || 0,
        taxCgstPercentage: currentRow.taxCgstPercentage || 0,
        taxSgstPercentage: currentRow.taxSgstPercentage || 0,
        taxIgstPercentage: currentRow.taxIgstPercentage || 0,
        basicAmount: currentRow.basicAmount || 0,
        cgst: currentRow.cgst || 0,
        sgst: currentRow.sgst || 0,
        igst: currentRow.igst || 0,
        totalAmount: currentRow.totalAmount || 0,
        totalInvoiceValue: currentRow.totalInvoiceValue || 0,
        netPayable: currentRow.netPayable || 0,
        remarks: currentRow.remarks || "",
      });
    } else {
      form.reset({
        invoiceNumber: "",
        invoiceDate: "",
        invoiceReceivedDate: "",
        orderNumber: "",
        vendorName: "",
        payeeId: 0,
        paymentDetailsId: 0,
        paymentDueDate: "",
        paymentStatus: "",
        quantity: 0,
        unit: "",
        unitPrice: 0,
        taxCgstPercentage: 0,
        taxSgstPercentage: 0,
        taxIgstPercentage: 0,
        basicAmount: 0,
        cgst: 0,
        sgst: 0,
        igst: 0,
        totalAmount: 0,
        totalInvoiceValue: 0,
        netPayable: 0,
        remarks: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: invoicesApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["invoices"] });
      toast.success("Invoice created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: InvoiceFormData }) =>
      invoicesApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["invoices"] });
      toast.success("Invoice updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: InvoiceFormData) => {
    if (isUpdate && currentRow) {
      updateMutation.mutate({ id: currentRow.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent className="flex flex-col overflow-hidden sm:max-w-3xl">
        <SheetHeader className="flex-shrink-0 text-start">
          <SheetTitle>
            {isUpdate ? "Update" : "Create"} Invoice
          </SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the invoice details."
              : "Add a new invoice."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="invoice-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 overflow-y-auto px-4"
          >
            <Tabs defaultValue="basic" className="w-full">
              <TabsList className="grid w-full grid-cols-3">
                <TabsTrigger value="basic">Basic</TabsTrigger>
                <TabsTrigger value="financial">Financial</TabsTrigger>
                <TabsTrigger value="other">Other</TabsTrigger>
              </TabsList>

              <TabsContent value="basic">
                <InvoiceBasicTab 
                  form={form} 
                  payees={payees} 
                  paymentStatuses={paymentStatuses}
                />
              </TabsContent>

              <TabsContent value="financial">
                <InvoiceFinancialTab form={form} />
              </TabsContent>

              <TabsContent value="other">
                <InvoiceOtherTab form={form} />
              </TabsContent>
            </Tabs>
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
            form="invoice-form"
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
