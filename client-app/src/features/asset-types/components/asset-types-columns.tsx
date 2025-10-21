import { ColumnDef } from '@tanstack/react-table';
import { DataTableColumnHeader } from '@/components/data-table';
import type { AssetType } from '../api/schema';
import { format } from 'date-fns';

export const assetTypesColumns: ColumnDef<AssetType>[] = [
  {
    accessorKey: 'typeName',
    header: ({ column }) => <DataTableColumnHeader column={column} title="Type Name" />,
    cell: ({ row }) => <div className="font-medium">{row.getValue('typeName')}</div>,
  },
  {
    accessorKey: 'typeCode',
    header: ({ column }) => <DataTableColumnHeader column={column} title="Type Code" />,
    cell: ({ row }) => <div className="font-mono">{row.getValue('typeCode')}</div>,
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
