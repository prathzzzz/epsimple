import type { ColumnDef } from '@tanstack/react-table';
import type { Location } from '../api/schema';
import { DataTableColumnHeader } from '@/components/data-table';
import { LocationRowActions } from './location-row-actions';

export const locationColumns: ColumnDef<Location>[] = [
  {
    accessorKey: 'locationName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Location Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[250px] truncate font-medium">
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
    accessorKey: 'district',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="District" />
    ),
    cell: ({ row }) => {
      const district = row.getValue('district') as string;
      return (
        <div className="max-w-[150px] truncate">
          {district || <span className="text-muted-foreground">-</span>}
        </div>
      );
    },
  },
  {
    accessorKey: 'pincode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Pincode" />
    ),
    cell: ({ row }) => {
      const pincode = row.getValue('pincode') as string;
      return (
        <div className="flex space-x-2">
          {pincode ? (
            <span className="inline-flex items-center rounded-md bg-green-50 px-2 py-1 text-xs font-medium text-green-700 ring-1 ring-inset ring-green-700/10 dark:bg-green-400/10 dark:text-green-400 dark:ring-green-400/30">
              {pincode}
            </span>
          ) : (
            <span className="text-muted-foreground">-</span>
          )}
        </div>
      );
    },
  },
  {
    accessorKey: 'address',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Address" />
    ),
    cell: ({ row }) => {
      const address = row.getValue('address') as string;
      return (
        <div className="max-w-[300px] truncate">
          {address || <span className="text-muted-foreground">-</span>}
        </div>
      );
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <LocationRowActions row={row} />,
  },
];
