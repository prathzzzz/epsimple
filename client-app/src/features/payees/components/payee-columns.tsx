import { ColumnDef } from '@tanstack/react-table';
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
  },
  {
    accessorKey: 'accountNumber',
    header: 'Account Number',
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
