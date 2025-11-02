import type { ColumnDef } from '@tanstack/react-table';
import { format } from 'date-fns';
import type { AssetsOnDatacenter } from '../api/schema';
import { DataTableColumnHeader } from '@/components/data-table';
import { AssetsOnDatacenterRowActions } from './assets-on-datacenter-row-actions';
import { Badge } from '@/components/ui/badge';

export const assetsOnDatacenterColumns: ColumnDef<AssetsOnDatacenter>[] = [
  {
    accessorKey: 'assetTagId',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Tag" />
    ),
    cell: ({ row }) => (
      <div className="font-mono text-sm font-medium">
        {row.getValue('assetTagId')}
      </div>
    ),
  },
  {
    accessorKey: 'assetName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[200px] truncate">
        {row.getValue('assetName')}
      </div>
    ),
  },
  {
    accessorKey: 'assetTypeName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Type" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[150px] truncate">
        {row.getValue('assetTypeName')}
      </div>
    ),
  },
  {
    accessorKey: 'DatacenterCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Datacenter Code" />
    ),
    cell: ({ row }) => {
      const code = row.original.datacenterCode;
      return (
        <div className="font-mono text-sm">
          {code || <span className="text-muted-foreground">-</span>}
        </div>
      );
    },
  },
  {
    accessorKey: 'DatacenterName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Datacenter Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[200px] truncate">
        {row.getValue('DatacenterName')}
      </div>
    ),
  },
  {
    accessorKey: 'assetStatusName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Status" />
    ),
    cell: ({ row }) => {
      return (
        <Badge variant="outline">
          {row.original.assetStatusName}
        </Badge>
      );
    },
  },
  {
    accessorKey: 'assignedOn',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Assigned" />
    ),
    cell: ({ row }) => {
      const date = row.original.assignedOn;
      return date ? (
        <div className="text-sm">
          {format(new Date(date), 'MMM dd, yyyy')}
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: 'commissionedOn',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Commissioned" />
    ),
    cell: ({ row }) => {
      const date = row.original.commissionedOn;
      return date ? (
        <div className="text-sm">
          {format(new Date(date), 'MMM dd, yyyy')}
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <AssetsOnDatacenterRowActions row={row} />,
  },
];
