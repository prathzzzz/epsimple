import { type Row } from "@tanstack/react-table";
import { MoreHorizontal, Pencil, Trash } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useAssetCategoryContext } from "../context/asset-category-provider";
import type { AssetCategory } from "../api/schema";

interface AssetCategoryRowActionsProps {
  row: Row<AssetCategory>;
}

export function AssetCategoryRowActions({ row }: AssetCategoryRowActionsProps) {
  const assetCategory = row.original;
  const { setEditingAssetCategory, setIsDrawerOpen, setIsDeleteDialogOpen } =
    useAssetCategoryContext();

  const handleEdit = () => {
    setEditingAssetCategory(assetCategory);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setEditingAssetCategory(assetCategory);
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
