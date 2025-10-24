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
      return (
        <div className="font-mono text-sm">
          {panNumber || <span className="text-muted-foreground">-</span>}
        </div>
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
      return (
        <div className="font-mono text-sm">
          {aadhaar || <span className="text-muted-foreground">-</span>}
        </div>
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
      return (
        <div className="font-mono text-sm">
          {ifsc || <span className="text-muted-foreground">-</span>}
        </div>
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
      return (
        <div className="font-mono text-sm">
          {accountNumber || <span className="text-muted-foreground">-</span>}
        </div>
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
