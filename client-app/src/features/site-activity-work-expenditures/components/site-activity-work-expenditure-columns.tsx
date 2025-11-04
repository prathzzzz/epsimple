import type { ColumnDef } from '@tanstack/react-table';
import type { SiteActivityWorkExpenditure } from '../api/schema';
import { SiteActivityWorkExpenditureRowActions } from './site-activity-work-expenditure-row-actions.tsx';

export const siteActivityWorkExpenditureColumns: ColumnDef<SiteActivityWorkExpenditure>[] = [
  {
    accessorKey: 'siteCode',
    header: 'Site Code',
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[200px] truncate font-medium">
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
        <div className="flex space-x-2">
          <span className="max-w-[200px] truncate font-medium">
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
