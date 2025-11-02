import type { ColumnDef } from '@tanstack/react-table';
import { format } from 'date-fns';
import type { AssetsOnSite } from '../api/schema';
import { DataTableColumnHeader } from '@/components/data-table';
import { AssetsOnSiteRowActions } from './assets-on-site-row-actions';
import { Badge } from '@/components/ui/badge';

export const assetsOnSiteColumns: ColumnDef<AssetsOnSite>[] = [
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
    accessorKey: 'siteCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Site Code" />
    ),
    cell: ({ row }) => (
      <div className="font-mono text-sm">
        {row.getValue('siteCode')}
      </div>
    ),
  },
  {
    accessorKey: 'siteName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Site Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[200px] truncate">
        {row.getValue('siteName')}
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
    accessorKey: 'deployedOn',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Deployed" />
    ),
    cell: ({ row }) => {
      const date = row.original.deployedOn;
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
    cell: ({ row }) => <AssetsOnSiteRowActions row={row} />,
  },
];
