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
import type { Warehouse } from '../api/schema';
import { useWarehouse } from '../hooks/use-warehouse';

interface WarehouseRowActionsProps {
  row: Row<Warehouse>;
}

export function WarehouseRowActions({ row }: WarehouseRowActionsProps) {
  const warehouse = row.original;
  const { setSelectedWarehouse, openDrawer, openDeleteDialog } = useWarehouse();

  const handleEdit = () => {
    setSelectedWarehouse(warehouse);
    openDrawer();
  };

  const handleDelete = () => {
    setSelectedWarehouse(warehouse);
    openDeleteDialog();
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
          <Pencil className="mr-2 h-4 w-4" />
          Edit
        </DropdownMenuItem>
        <DropdownMenuItem onClick={handleDelete} className="text-destructive">
          <Trash className="mr-2 h-4 w-4" />
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
