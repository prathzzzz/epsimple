import { Row } from "@tanstack/react-table";
import { MoreHorizontal, Pen, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useSiteCategoryContext } from "../context/site-category-provider";
import type { SiteCategory } from "../api/schema";

interface SiteCategoryRowActionsProps {
  row: Row<SiteCategory>;
}

export function SiteCategoryRowActions({ row }: SiteCategoryRowActionsProps) {
  const { setEditingCategory, setIsDrawerOpen, setIsDeleteDialogOpen } = useSiteCategoryContext();
  const siteCategory = row.original;

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
        <DropdownMenuItem
          onClick={() => {
            setEditingCategory(siteCategory);
            setIsDrawerOpen(true);
          }}
        >
          <Pen className="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
          Edit
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          onClick={() => {
            setEditingCategory(siteCategory);
            setIsDeleteDialogOpen(true);
          }}
        >
          <Trash2 className="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
