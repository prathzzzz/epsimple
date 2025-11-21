import { type Row } from "@tanstack/react-table";
import { MoreHorizontal, Pencil, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { PermissionGuard } from "@/components/permission-guard";
import type { PersonDetails } from "../api/schema";
import { usePersonDetailsContext } from "../context/person-details-provider";

interface PersonDetailsRowActionsProps {
  row: Row<PersonDetails>;
}

export function PersonDetailsRowActions({ row }: PersonDetailsRowActionsProps) {
  const personDetails = row.original;
  const {
    setIsDrawerOpen,
    setEditingPersonDetails,
    setIsDeleteDialogOpen,
    setDeletingPersonDetailsId,
  } = usePersonDetailsContext();

  const handleEdit = () => {
    setEditingPersonDetails(personDetails);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setDeletingPersonDetailsId(personDetails.id);
    setIsDeleteDialogOpen(true);
  };

  return (
    <PermissionGuard anyPermissions={['PERSON_DETAILS:UPDATE', 'PERSON_DETAILS:DELETE']}>
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
          <PermissionGuard permission="PERSON_DETAILS:UPDATE">
            <DropdownMenuItem onClick={handleEdit}>
              <Pencil className="mr-2 h-4 w-4" />
              Edit
            </DropdownMenuItem>
          </PermissionGuard>
          <PermissionGuard permission="PERSON_DETAILS:DELETE">
            <DropdownMenuItem onClick={handleDelete} className="text-destructive">
              <Trash2 className="mr-2 h-4 w-4" />
              Delete
            </DropdownMenuItem>
          </PermissionGuard>
        </DropdownMenuContent>
      </DropdownMenu>
    </PermissionGuard>
  );
}
