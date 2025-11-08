import { ColumnDef } from '@tanstack/react-table';
import { DataTableColumnHeader } from '@/components/data-table';
import type { VendorType } from '../api/schema';
import { format } from 'date-fns';

export const vendorTypesColumns: ColumnDef<VendorType>[] = [
  {
    accessorKey: 'typeName',
    header: ({ column }) => <DataTableColumnHeader column={column} title="Type Name" />,
    cell: ({ row }) => (
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
          {row.getValue('typeName')}
        </span>
      </div>
    ),
  },
  {
    accessorKey: 'vendorCategory.categoryName',
    header: ({ column }) => <DataTableColumnHeader column={column} title="Category" />,
    cell: ({ row }) => {
      const vendorCategory = row.original.vendorCategory;
      return vendorCategory?.categoryName ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
            {vendorCategory.categoryName}
          </span>
        </div>
      ) : (
        <span className='text-muted-foreground'>-</span>
      );
    },
  },
  {
    accessorKey: 'description',
    header: ({ column }) => <DataTableColumnHeader column={column} title="Description" />,
    cell: ({ row }) => {
      const description = row.getValue('description') as string;
      return (
        <div className="max-w-[500px] truncate" title={description}>
          {description || '-'}
        </div>
      );
    },
  },
  {
    accessorKey: 'createdAt',
    header: ({ column }) => <DataTableColumnHeader column={column} title="Created At" />,
    cell: ({ row }) => {
      const date = row.getValue('createdAt') as string;
      return <div>{format(new Date(date), 'PPp')}</div>;
    },
  },
  {
    accessorKey: 'updatedAt',
    header: ({ column }) => <DataTableColumnHeader column={column} title="Updated At" />,
    cell: ({ row }) => {
      const date = row.getValue('updatedAt') as string;
      return <div>{format(new Date(date), 'PPp')}</div>;
    },
  },
];
