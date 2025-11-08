import type { ColumnDef } from '@tanstack/react-table';
import { format } from 'date-fns';
import type { AssetsOnDatacenter } from '../api/schema';
import { DataTableColumnHeader } from '@/components/data-table';
import { AssetsOnDatacenterRowActions } from './assets-on-datacenter-row-actions';

export const assetsOnDatacenterColumns: ColumnDef<AssetsOnDatacenter>[] = [
  {
    accessorKey: 'assetTagId',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Tag" />
    ),
    cell: ({ row }) => (
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
          {row.getValue('assetTagId')}
        </span>
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
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
          {row.getValue('assetTypeName')}
        </span>
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
      return code ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {code}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
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
    cell: ({ row }) => (
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-green-50 px-2 py-1 text-xs font-medium text-green-700 ring-1 ring-inset ring-green-700/10 dark:bg-green-400/10 dark:text-green-400 dark:ring-green-400/30'>
          {row.original.assetStatusName}
        </span>
      </div>
    ),
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
