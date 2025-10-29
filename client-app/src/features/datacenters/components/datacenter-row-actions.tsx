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
import type { Datacenter } from '../api/schema';
import { useDatacenter } from '../context/datacenter-provider';

interface DatacenterRowActionsProps {
  row: Row<Datacenter>;
}

export function DatacenterRowActions({ row }: DatacenterRowActionsProps) {
  const datacenter = row.original;
  const { setSelectedDatacenter, openDrawer, openDeleteDialog } = useDatacenter();

  const handleEdit = () => {
    setSelectedDatacenter(datacenter);
    openDrawer();
  };

  const handleDelete = () => {
    setSelectedDatacenter(datacenter);
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
