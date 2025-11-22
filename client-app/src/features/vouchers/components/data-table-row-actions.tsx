import type { Row } from "@tanstack/react-table";
import { MoreHorizontal, Pencil, Trash } from "lucide-react";
import { PermissionGuard } from "@/components/permission-guard";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import type { Voucher } from "../api/schema";
import { useVoucher } from "../hooks/use-voucher";

interface DataTableRowActionsProps {
  row: Row<Voucher>;
}

export function DataTableRowActions({ row }: DataTableRowActionsProps) {
  const voucher = row.original;
  const {
    setIsDrawerOpen,
    setEditingVoucher,
    setIsDeleteDialogOpen,
    setVoucherToDelete,
  } = useVoucher();

  const handleEdit = () => {
    setEditingVoucher(voucher);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setVoucherToDelete(voucher);
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
        <PermissionGuard permission="VOUCHER:UPDATE">
          <DropdownMenuItem onClick={handleEdit}>
            <Pencil className="mr-2 h-4 w-4" />
            Edit
          </DropdownMenuItem>
        </PermissionGuard>
        <PermissionGuard permission="VOUCHER:DELETE">
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
