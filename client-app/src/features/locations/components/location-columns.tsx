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
        <div className="font-mono text-sm">
          {pincode || <span className="text-muted-foreground">-</span>}
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
