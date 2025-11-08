import type { ColumnDef } from '@tanstack/react-table';
import type { SiteActivityWorkExpenditure } from '../api/schema';
import { SiteActivityWorkExpenditureRowActions } from './site-activity-work-expenditure-row-actions.tsx';

export const siteActivityWorkExpenditureColumns: ColumnDef<SiteActivityWorkExpenditure>[] = [
  {
    accessorKey: 'siteCode',
    header: 'Site Code',
    cell: ({ row }) => {
      return (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {row.getValue('siteCode')}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: 'siteName',
    header: 'Site Name',
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[300px] truncate">
            {row.getValue('siteName')}
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
          <span className="max-w-[250px] truncate">
            {row.getValue('activityName')}
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
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {row.getValue('invoiceNumber')}
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
      const formatted = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
      }).format(amount);

      return <div className="text-right font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: 'costItemName',
    header: 'Cost Item',
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[250px] truncate">
            {row.getValue('costItemName')}
          </span>
        </div>
      );
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <SiteActivityWorkExpenditureRowActions row={row} />,
  },
];
