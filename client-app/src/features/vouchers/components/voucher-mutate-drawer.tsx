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

import { vouchersApi } from "../api/vouchers-api";
import { payeeApi } from "@/features/payees/api/payee-api";
import { genericStatusTypeApi } from "@/features/generic-status-types/api/generic-status-type-api";
import { paymentDetailsApi } from "@/features/payment-details/api/payment-details-api";
import {
  voucherFormSchema,
  type VoucherFormData,
  type Voucher,
} from "../api/schema";
import { VoucherBasicTab } from "./voucher-basic-tab";
import { VoucherFinancialTab } from "./voucher-financial-tab";
import { VoucherOtherTab } from "./voucher-other-tab";

interface VoucherMutateDrawerProps {
  readonly open: boolean;
  readonly onOpenChange: (open: boolean) => void;
  readonly currentRow: Voucher | null;
}

export function VoucherMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: VoucherMutateDrawerProps) {
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

  // Fetch payment details for dropdown
  const { data: paymentDetailsData } = useQuery({
    queryKey: ["payment-details", "list"],
    queryFn: () => paymentDetailsApi.getList(),
  });

  const payees = payeesData?.data || [];
  const paymentStatuses = statusesData?.data || [];
  const paymentDetails = paymentDetailsData?.data || [];

  const form = useForm<VoucherFormData>({
    resolver: zodResolver(voucherFormSchema),
    defaultValues: {
      voucherNumber: "",
      voucherDate: "",
      orderNumber: "",
      payeeId: 0,
      paymentDetailsId: undefined,
      paymentDueDate: "",
      paymentStatus: "",
      quantity: 0,
      unit: "",
      unitPrice: 0,
      taxCgst: 0,
      taxSgst: 0,
      taxIgst: 0,
      amount1: 0,
      amount2: 0,
      discountPercentage: 0,
      discountAmount: 0,
      finalAmount: 0,
      remarks: "",
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        voucherNumber: currentRow.voucherNumber,
        voucherDate: currentRow.voucherDate,
        orderNumber: currentRow.orderNumber || "",
        payeeId: currentRow.payeeId,
        paymentDetailsId: currentRow.paymentDetailsId || undefined,
        paymentDueDate: currentRow.paymentDueDate || "",
        paymentStatus: currentRow.paymentStatus || "",
        quantity: currentRow.quantity || 0,
        unit: currentRow.unit || "",
        unitPrice: currentRow.unitPrice || 0,
        taxCgst: currentRow.taxCgst || 0,
        taxSgst: currentRow.taxSgst || 0,
        taxIgst: currentRow.taxIgst || 0,
        amount1: currentRow.amount1 || 0,
        amount2: currentRow.amount2 || 0,
        discountPercentage: currentRow.discountPercentage || 0,
        discountAmount: currentRow.discountAmount || 0,
        finalAmount: currentRow.finalAmount || 0,
        remarks: currentRow.remarks || "",
      });
    } else {
      form.reset({
        voucherNumber: "",
        voucherDate: "",
        orderNumber: "",
        payeeId: 0,
        paymentDetailsId: undefined,
        paymentDueDate: "",
        paymentStatus: "",
        quantity: 0,
        unit: "",
        unitPrice: 0,
        taxCgst: 0,
        taxSgst: 0,
        taxIgst: 0,
        amount1: 0,
        amount2: 0,
        discountPercentage: 0,
        discountAmount: 0,
        finalAmount: 0,
        remarks: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: vouchersApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["vouchers"] });
      toast.success("Voucher created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: VoucherFormData }) =>
      vouchersApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["vouchers"] });
      toast.success("Voucher updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: VoucherFormData) => {
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
          <SheetTitle>{isUpdate ? "Update" : "Create"} Voucher</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the voucher details."
              : "Add a new voucher."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="voucher-form"
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
                <VoucherBasicTab
                  form={form}
                  payees={payees}
                  paymentStatuses={paymentStatuses}
                  paymentDetails={paymentDetails}
                />
              </TabsContent>

              <TabsContent value="financial">
                <VoucherFinancialTab form={form} />
              </TabsContent>

              <TabsContent value="other">
                <VoucherOtherTab form={form} />
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
            form="voucher-form"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {(() => {
              if (createMutation.isPending || updateMutation.isPending) {
                return (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Saving...
                  </>
                );
              }
              return isUpdate ? "Update" : "Create";
            })()}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
