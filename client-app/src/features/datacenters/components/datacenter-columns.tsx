import type { ColumnDef } from '@tanstack/react-table';
import type { Datacenter } from '../api/schema';
import { DataTableColumnHeader } from '@/components/data-table';
import { DatacenterRowActions } from './datacenter-row-actions';

export const datacenterColumns: ColumnDef<Datacenter>[] = [
  {
    accessorKey: 'datacenterName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Datacenter Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[250px] truncate font-medium">
        {row.getValue('datacenterName')}
      </div>
    ),
  },
  {
    accessorKey: 'datacenterCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Code" />
    ),
    cell: ({ row }) => {
      const code = row.getValue('datacenterCode') as string;
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
    accessorKey: 'datacenterType',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Type" />
    ),
    cell: ({ row }) => {
      const type = row.getValue('datacenterType') as string;
      return type ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
            {type}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: 'locationName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Location" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[200px] truncate">
        {row.getValue('locationName')}
      </div>
    ),
  },
  {
    accessorKey: 'cityName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="City" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[150px] truncate">
        {row.getValue('cityName')}
      </div>
    ),
  },
  {
    accessorKey: 'stateName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="State" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[150px] truncate">
        {row.getValue('stateName')}
      </div>
    ),
  },
  {
    id: 'actions',
    cell: ({ row }) => <DatacenterRowActions row={row} />,
  },
];
