import type { ColumnDef } from '@tanstack/react-table';
import type { Warehouse } from '../api/schema';
import { DataTableColumnHeader } from '@/components/data-table';
import { WarehouseRowActions } from './warehouse-row-actions';

export const warehouseColumns: ColumnDef<Warehouse>[] = [
  {
    accessorKey: 'warehouseName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Warehouse Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[250px] truncate font-medium">
        {row.getValue('warehouseName')}
      </div>
    ),
  },
  {
    accessorKey: 'warehouseCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Code" />
    ),
    cell: ({ row }) => {
      const code = row.getValue('warehouseCode') as string;
      return (
        <div className="font-mono text-sm">
          {code || <span className="text-muted-foreground">-</span>}
        </div>
      );
    },
  },
  {
    accessorKey: 'warehouseType',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Type" />
    ),
    cell: ({ row }) => {
      const type = row.getValue('warehouseType') as string;
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
    cell: ({ row }) => <WarehouseRowActions row={row} />,
  },
];
