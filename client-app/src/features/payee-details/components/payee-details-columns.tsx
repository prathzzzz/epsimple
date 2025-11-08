import type { ColumnDef } from '@tanstack/react-table';
import type { PayeeDetails } from '../api/schema';
import { PayeeDetailsRowActions } from './payee-details-row-actions';
import { DataTableColumnHeader } from '@/components/data-table';

export const payeeDetailsColumns: ColumnDef<PayeeDetails>[] = [
  {
    accessorKey: 'payeeName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Payee Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[250px] truncate font-medium">
        {row.getValue('payeeName')}
      </div>
    ),
  },
  {
    accessorKey: 'panNumber',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="PAN" />
    ),
    cell: ({ row }) => {
      const panNumber = row.getValue('panNumber') as string;
      return panNumber ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {panNumber}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: 'aadhaarNumber',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Aadhaar" />
    ),
    cell: ({ row }) => {
      const aadhaar = row.getValue('aadhaarNumber') as string;
      return aadhaar ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {aadhaar}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: 'bankName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Bank" />
    ),
    cell: ({ row }) => {
      const bankName = row.getValue('bankName') as string;
      return (
        <div className="max-w-[200px] truncate">
          {bankName || <span className="text-muted-foreground">-</span>}
        </div>
      );
    },
  },
  {
    accessorKey: 'ifscCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="IFSC" />
    ),
    cell: ({ row }) => {
      const ifsc = row.getValue('ifscCode') as string;
      return ifsc ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {ifsc}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: 'accountNumber',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Account Number" />
    ),
    cell: ({ row }) => {
      const accountNumber = row.getValue('accountNumber') as string;
      return accountNumber ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {accountNumber}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: 'beneficiaryName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Beneficiary" />
    ),
    cell: ({ row }) => {
      const beneficiary = row.getValue('beneficiaryName') as string;
      return (
        <div className="max-w-[200px] truncate">
          {beneficiary || <span className="text-muted-foreground">-</span>}
        </div>
      );
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <PayeeDetailsRowActions row={row} />,
  },
];
