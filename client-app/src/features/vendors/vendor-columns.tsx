import { type ColumnDef } from '@tanstack/react-table';
import { ArrowUpDown } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { type Vendor } from '@/features/vendors/api/vendors-api';
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
      return (
        <div className='flex space-x-2'>
          {code ? (
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {code}
            </span>
          ) : (
            <span className='text-muted-foreground'>-</span>
          )}
        </div>
      );
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
    cell: ({ row }) => {
      const type = row.getValue('vendorTypeName') as string;
      return (
        <div className='flex space-x-2'>
          {type ? (
            <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
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
    cell: ({ row }) => {
      const category = row.getValue('vendorCategoryName') as string;
      return (
        <div className='flex space-x-2'>
          {category ? (
            <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
              {category}
            </span>
          ) : (
            <span className='text-muted-foreground'>-</span>
          )}
        </div>
      );
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <VendorRowActions vendor={row.original} />,
  },
];
