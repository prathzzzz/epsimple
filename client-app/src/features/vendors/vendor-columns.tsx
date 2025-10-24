import { ColumnDef } from '@tanstack/react-table';
import { ArrowUpDown } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Vendor } from '@/lib/vendors-api';
import { VendorRowActions } from './vendor-row-actions';

export const vendorColumns: ColumnDef<Vendor>[] = [
  {
    accessorKey: 'vendorCodeAlt',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Vendor Code
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => {
      const code = row.getValue('vendorCodeAlt') as string | undefined;
      return <div>{code || '-'}</div>;
    },
  },
  {
    accessorKey: 'vendorName',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Vendor Name
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => <div>{row.getValue('vendorName')}</div>,
  },
  {
    accessorKey: 'vendorEmail',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Email
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => {
      const email = row.getValue('vendorEmail') as string | undefined;
      return <div>{email || '-'}</div>;
    },
  },
  {
    accessorKey: 'vendorContact',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Contact
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => {
      const contact = row.getValue('vendorContact') as string | undefined;
      return <div>{contact || '-'}</div>;
    },
  },
  {
    accessorKey: 'vendorTypeName',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Vendor Type
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => <div>{row.getValue('vendorTypeName')}</div>,
  },
  {
    accessorKey: 'vendorCategoryName',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Category
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => <div>{row.getValue('vendorCategoryName')}</div>,
  },
  {
    id: 'actions',
    cell: ({ row }) => <VendorRowActions vendor={row.original} />,
  },
];
