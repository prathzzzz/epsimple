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
      return orderNumber ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {orderNumber}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
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
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-green-50 px-2 py-1 text-xs font-medium text-green-700 ring-1 ring-inset ring-green-700/10 dark:bg-green-400/10 dark:text-green-400 dark:ring-green-400/30'>
          {row.getValue('statusTypeName')}
        </span>
      </div>
    ),
  },
  {
    id: 'actions',
    cell: ({ row }) => <ActivityWorkRowActions row={row} />,
  },
];
