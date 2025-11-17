import { DotsHorizontalIcon } from "@radix-ui/react-icons";
import { type Row } from "@tanstack/react-table";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useAssetTagCodeGeneratorContext } from "../context/asset-tag-generator-provider";
import type { AssetTagCodeGenerator } from "../api/schema";

interface AssetTagCodeGeneratorRowActionsProps {
  row: Row<AssetTagCodeGenerator>;
}

export function AssetTagCodeGeneratorRowActions({ row }: AssetTagCodeGeneratorRowActionsProps) {
  const { setEditingGenerator, setIsDrawerOpen, setGeneratorToDelete, setIsDeleteDialogOpen } =
    useAssetTagCodeGeneratorContext();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="flex h-8 w-8 p-0 data-[state=open]:bg-muted"
        >
          <DotsHorizontalIcon className="h-4 w-4" />
          <span className="sr-only">Open menu</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-[160px]">
        <DropdownMenuItem
          onClick={() => {
            setEditingGenerator(row.original);
            setIsDrawerOpen(true);
          }}
        >
          Edit
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          onClick={() => {
            setGeneratorToDelete(row.original);
            setIsDeleteDialogOpen(true);
          }}
          className="text-destructive focus:text-destructive"
        >
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
