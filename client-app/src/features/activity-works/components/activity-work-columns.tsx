import type { ColumnDef } from '@tanstack/react-table';
import type { ActivityWork } from '../api/schema';
import { DataTableColumnHeader } from '@/components/data-table';
import { ActivityWorkRowActions } from './activity-work-row-actions';
import { format } from 'date-fns';

export const activityWorkColumns: ColumnDef<ActivityWork>[] = [
  {
    accessorKey: 'activitiesName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Activity" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[200px] truncate font-medium">
        {row.getValue('activitiesName')}
      </div>
    ),
  },
  {
    accessorKey: 'vendorName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Vendor" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[200px] truncate">
        {row.getValue('vendorName')}
      </div>
    ),
  },
  {
    accessorKey: 'vendorOrderNumber',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Order Number" />
    ),
    cell: ({ row }) => {
      const orderNumber = row.getValue('vendorOrderNumber') as string;
      return (
        <div className="font-mono text-sm">
          {orderNumber || <span className="text-muted-foreground">-</span>}
        </div>
      );
    },
  },
  {
    accessorKey: 'workOrderDate',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Order Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue('workOrderDate') as string;
      return date ? format(new Date(date), 'PP') : <span className="text-muted-foreground">-</span>;
    },
  },
  {
    accessorKey: 'workStartDate',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Start Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue('workStartDate') as string;
      return date ? format(new Date(date), 'PP') : <span className="text-muted-foreground">-</span>;
    },
  },
  {
    accessorKey: 'workCompletionDate',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Completion Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue('workCompletionDate') as string;
      return date ? format(new Date(date), 'PP') : <span className="text-muted-foreground">-</span>;
    },
  },
  {
    accessorKey: 'statusTypeName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Status" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[150px] truncate">
        {row.getValue('statusTypeName')}
      </div>
    ),
  },
  {
    id: 'actions',
    cell: ({ row }) => <ActivityWorkRowActions row={row} />,
  },
];
