import { Row } from "@tanstack/react-table";
import { MoreHorizontal, Pencil, Trash } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import type { GenericStatusType } from "../api/schema";
import { useGenericStatusType } from "../context/generic-status-type-provider";

interface DataTableRowActionsProps {
  row: Row<GenericStatusType>;
}

export function DataTableRowActions({ row }: DataTableRowActionsProps) {
  const statusType = row.original;
  const {
    setSelectedStatusType,
    setIsDrawerOpen,
    setIsDeleteDialogOpen,
    setIsEditMode,
  } = useGenericStatusType();

  const handleEdit = () => {
    setSelectedStatusType(statusType);
    setIsEditMode(true);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setSelectedStatusType(statusType);
    setIsDeleteDialogOpen(true);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="ghost" className="h-8 w-8 p-0">
          <span className="sr-only">Open menu</span>
          <MoreHorizontal className="h-4 w-4" />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <DropdownMenuLabel>Actions</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleEdit}>
          <Pencil className="h-4 w-4" />
          Edit
        </DropdownMenuItem>
        <DropdownMenuItem
          onClick={handleDelete}
          className="text-destructive focus:text-destructive"
        >
          <Trash className="h-4 w-4" />
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
