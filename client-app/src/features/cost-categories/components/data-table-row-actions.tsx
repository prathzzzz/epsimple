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

import { useCostCategories } from "../context/cost-categories-provider";
import type { CostCategory } from "../api/schema";

interface DataTableRowActionsProps {
  row: Row<CostCategory>;
}

export function DataTableRowActions({ row }: DataTableRowActionsProps) {
  const costCategory = row.original;
  const {
    setSelectedCostCategory,
    setIsDrawerOpen,
    setIsDeleteDialogOpen,
    setIsEditMode,
  } = useCostCategories();

  const handleEdit = () => {
    setSelectedCostCategory(costCategory);
    setIsEditMode(true);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setSelectedCostCategory(costCategory);
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
