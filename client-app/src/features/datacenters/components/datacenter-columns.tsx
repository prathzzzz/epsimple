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
      return (
        <div className="font-mono text-sm">
          {code || <span className="text-muted-foreground">-</span>}
        </div>
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
      return (
        <div className="max-w-[150px] truncate">
          {type || <span className="text-muted-foreground">-</span>}
        </div>
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
