import { MoreHorizontal, Pen, Trash2 } from "lucide-react";
import type { Row } from "@tanstack/react-table";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { PermissionGuard } from "@/components/permission-guard";
import { useManagedProjectContext } from "../context/managed-project-provider";
import type { ManagedProject } from "../api/schema";

interface ManagedProjectRowActionsProps {
  row: Row<ManagedProject>;
}

export function ManagedProjectRowActions({ row }: ManagedProjectRowActionsProps) {
  const { setIsDrawerOpen, setEditingManagedProject, setIsDeleteDialogOpen, setDeletingManagedProjectId } =
    useManagedProjectContext();

  const handleEdit = () => {
    setEditingManagedProject(row.original);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setDeletingManagedProjectId(row.original.id);
    setIsDeleteDialogOpen(true);
  };

  return (
    <PermissionGuard anyPermissions={["MANAGED_PROJECT:UPDATE", "MANAGED_PROJECT:DELETE"]}>
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
          <PermissionGuard permission="MANAGED_PROJECT:UPDATE">
            <DropdownMenuItem onClick={handleEdit}>
              <Pen className="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
              Edit
            </DropdownMenuItem>
          </PermissionGuard>
          <PermissionGuard permission="MANAGED_PROJECT:DELETE">
            <DropdownMenuItem onClick={handleDelete}>
              <Trash2 className="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
              Delete
            </DropdownMenuItem>
          </PermissionGuard>
        </DropdownMenuContent>
      </DropdownMenu>
    </PermissionGuard>
  );
}
