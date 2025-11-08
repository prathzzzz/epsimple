import type { ColumnDef } from '@tanstack/react-table';
import type { AssetExpenditureAndActivityWork } from '../api/schema';
import { AssetExpenditureAndActivityWorkRowActions } from './asset-expenditure-and-activity-work-row-actions';

export const assetExpenditureAndActivityWorkColumns: ColumnDef<AssetExpenditureAndActivityWork>[] = [
  {
    accessorKey: 'assetTagId',
    header: 'Asset Tag',
    cell: ({ row }) => {
      return (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {row.getValue('assetTagId')}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: 'assetName',
    header: 'Asset Name',
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[250px] truncate">
            {row.getValue('assetName')}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: 'activityName',
    header: 'Activity',
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[200px] truncate">
            {row.getValue('activityName') || '-'}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: 'invoiceNumber',
    header: 'Invoice Number',
    cell: ({ row }) => {
      const invoiceNumber = row.getValue('invoiceNumber') as string;
      return invoiceNumber ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {invoiceNumber}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: 'amount',
    header: 'Amount',
    cell: ({ row }) => {
      const amount = parseFloat(row.getValue('amount'));
      if (isNaN(amount)) return <div className="text-right">-</div>;
      const formatted = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
      }).format(amount);

      return <div className="text-right font-medium">{formatted}</div>;
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <AssetExpenditureAndActivityWorkRowActions row={row} />,
  },
];
