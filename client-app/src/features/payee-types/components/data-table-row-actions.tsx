import { Row } from "@tanstack/react-table";
import { MoreHorizontal, Pencil, Trash } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import { usePayeeTypes } from "../context/payee-types-provider";
import type { PayeeType } from "../api/schema";

interface DataTableRowActionsProps {
  row: Row<PayeeType>;
}

export function DataTableRowActions({ row }: DataTableRowActionsProps) {
  const payeeType = row.original;
  const {
    setSelectedPayeeType,
    setIsDrawerOpen,
    setIsDeleteDialogOpen,
    setIsEditMode,
  } = usePayeeTypes();

  const handleEdit = () => {
    setSelectedPayeeType(payeeType);
    setIsEditMode(true);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setSelectedPayeeType(payeeType);
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
        <DropdownMenuItem onClick={handleEdit}>
          <Pencil className="mr-2 h-4 w-4" />
          Edit
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleDelete} className="text-destructive">
          <Trash className="mr-2 h-4 w-4" />
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
