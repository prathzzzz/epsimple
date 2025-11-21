import type { Row } from "@tanstack/react-table";
import { MoreHorizontal, Pencil, Trash } from "lucide-react";

import { Button } from "@/components/ui/button";
import { PermissionGuard } from "@/components/permission-guard";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import { usePaymentDetails } from "../context/payment-details-provider";
import type { PaymentDetails } from "../api/schema";

interface DataTableRowActionsProps {
  row: Row<PaymentDetails>;
}

export function DataTableRowActions({ row }: DataTableRowActionsProps) {
  const paymentDetails = row.original;
  const {
    setSelectedPaymentDetails,
    setIsDrawerOpen,
    setIsDeleteDialogOpen,
    setIsEditMode,
  } = usePaymentDetails();

  const handleEdit = () => {
    setSelectedPaymentDetails(paymentDetails);
    setIsEditMode(true);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setSelectedPaymentDetails(paymentDetails);
    setIsDeleteDialogOpen(true);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="flex h-8 w-8 p-0 data-[state=open]:bg-muted"
        >
          <MoreHorizontal className="h-4 w-4" />
          <span className="sr-only">Open menu</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-[160px]">
        <PermissionGuard permission="PAYMENT:UPDATE">
          <DropdownMenuItem onClick={handleEdit}>
            <Pencil className="mr-2 h-4 w-4" />
            Edit
          </DropdownMenuItem>
        </PermissionGuard>
        <PermissionGuard permission="PAYMENT:DELETE">
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleDelete} className="text-destructive">
            <Trash className="mr-2 h-4 w-4" />
            Delete
          </DropdownMenuItem>
        </PermissionGuard>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
