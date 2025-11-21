import { MoreHorizontal, Pencil, Trash } from 'lucide-react';
import type { Row } from '@tanstack/react-table';

import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { PermissionGuard } from '@/components/permission-guard';
import type { AssetsOnDatacenter } from '../api/schema';
import { useAssetsOnDatacenter } from '../context/assets-on-datacenter-provider';

interface AssetsOnDatacenterRowActionsProps {
  readonly row: Row<AssetsOnDatacenter>;
}

export function AssetsOnDatacenterRowActions({ row }: AssetsOnDatacenterRowActionsProps) {
  const placement = row.original;
  const { setSelectedPlacement, openDrawer, openDeleteDialog } = useAssetsOnDatacenter();

  const handleEdit = () => {
    setSelectedPlacement(placement);
    openDrawer();
  };

  const handleDelete = () => {
    setSelectedPlacement(placement);
    openDeleteDialog();
  };

  return (
    <PermissionGuard anyPermissions={['ASSETS_ON_DATACENTER:UPDATE', 'ASSETS_ON_DATACENTER:DELETE']}>
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
          <PermissionGuard permission="ASSETS_ON_DATACENTER:UPDATE">
            <DropdownMenuItem onClick={handleEdit}>
              <Pencil className="mr-2 h-4 w-4" />
              Edit
            </DropdownMenuItem>
          </PermissionGuard>
          <PermissionGuard permission="ASSETS_ON_DATACENTER:DELETE">
            <DropdownMenuItem onClick={handleDelete} className="text-destructive">
              <Trash className="mr-2 h-4 w-4" />
              Remove
            </DropdownMenuItem>
          </PermissionGuard>
        </DropdownMenuContent>
      </DropdownMenu>
    </PermissionGuard>
  );
}
