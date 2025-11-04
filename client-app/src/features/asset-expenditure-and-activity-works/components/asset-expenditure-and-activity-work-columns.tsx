import type { ColumnDef } from '@tanstack/react-table';
import type { AssetExpenditureAndActivityWork } from '../api/schema';
import { AssetExpenditureAndActivityWorkRowActions } from './asset-expenditure-and-activity-work-row-actions.tsx';

export const assetExpenditureAndActivityWorkColumns: ColumnDef<AssetExpenditureAndActivityWork>[] = [
  {
    accessorKey: 'assetTagId',
    header: 'Asset Tag',
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[200px] truncate font-medium">
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
      return (
        <div className="flex space-x-2">
          <span className="max-w-[180px] truncate font-medium">
            {row.getValue('invoiceNumber') || '-'}
          </span>
        </div>
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
