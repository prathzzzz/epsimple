import { Edit2, Trash, Receipt, MoreHorizontal } from "lucide-react";
import { useNavigate } from "@tanstack/react-router";
import { Button } from "@/components/ui/button";
import { PermissionGuard } from "@/components/permission-guard";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useSiteContext } from "../context/site-provider";
import type { Site } from "../api/schema";

interface SiteRowActionsProps {
  site: Site;
}

export function SiteRowActions({ site }: SiteRowActionsProps) {
  const { setIsDrawerOpen, setEditingSite, setIsDeleteDialogOpen, setDeletingSiteId } =
    useSiteContext();
  const navigate = useNavigate();

  const handleEdit = () => {
    setEditingSite(site);
    setIsDrawerOpen(true);
  };

  const handleDelete = () => {
    setDeletingSiteId(site.id);
    setIsDeleteDialogOpen(true);
  };

  const handleManageExpenditures = () => {
    navigate({
      to: '/site-activity-work-expenditures',
      search: { siteId: site.id },
    });
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
        <PermissionGuard permission="SITE:UPDATE">
          <DropdownMenuItem onClick={handleEdit}>
            <Edit2 className="mr-2 h-4 w-4" />
            Edit
          </DropdownMenuItem>
        </PermissionGuard>
        <DropdownMenuItem onClick={handleManageExpenditures}>
          <Receipt className="mr-2 h-4 w-4" />
          Manage Expenditures
        </DropdownMenuItem>
        <PermissionGuard permission="SITE:DELETE">
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
