import { type ColumnDef } from '@tanstack/react-table';
import { PayeeRowActions } from './payee-row-actions';
import type { Payee } from '../api/schema';

export const payeeColumns: ColumnDef<Payee>[] = [
  {
    accessorKey: 'payeeName',
    header: 'Payee Name',
  },
  {
    accessorKey: 'payeeTypeName',
    header: 'Payee Type',
    cell: ({ row }) => {
      const type = row.original.payeeTypeName;
      return (
        <div className='flex space-x-2'>
          {type ? (
            <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
              {type}
            </span>
          ) : (
            <span className='text-muted-foreground'>-</span>
          )}
        </div>
      );
    },
  },
  {
    accessorKey: 'accountNumber',
    header: 'Account Number',
    cell: ({ row }) => {
      const accountNumber = row.original.accountNumber;
      return (
        <div className='flex space-x-2'>
          {accountNumber ? (
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {accountNumber}
            </span>
          ) : (
            <span className='text-muted-foreground'>-</span>
          )}
        </div>
      );
    },
  },
  {
    accessorKey: 'bankName',
    header: 'Bank',
    cell: ({ row }) => row.original.bankName || '-',
  },
  {
    accessorKey: 'vendorName',
    header: 'Vendor',
    cell: ({ row }) => row.original.vendorName || '-',
  },
  {
    accessorKey: 'landlordName',
    header: 'Landlord',
    cell: ({ row }) => row.original.landlordName || '-',
  },
  {
    id: 'actions',
    cell: ({ row }) => <PayeeRowActions payee={row.original} />,
  },
];
