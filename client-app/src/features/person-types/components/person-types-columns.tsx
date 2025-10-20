import { type ColumnDef } from '@tanstack/react-table'
import { DataTableColumnHeader } from '@/components/data-table'
import { DataTableRowActions } from './data-table-row-actions'
import type { PersonType } from '../data/schema'

export const personTypesColumns: ColumnDef<PersonType>[] = [
  {
    accessorKey: 'typeName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Type Name' />
    ),
    cell: ({ row }) => {
      return (
        <div className='flex space-x-2'>
          <span className='max-w-[500px] truncate font-medium'>
            {row.getValue('typeName')}
          </span>
        </div>
      )
    },
    enableSorting: true,
  },
  {
    accessorKey: 'description',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Description' />
    ),
    cell: ({ row }) => {
      const description = row.getValue('description') as string | undefined
      return (
        <div className='flex space-x-2'>
          <span className='max-w-[500px] truncate'>
            {description || '-'}
          </span>
        </div>
      )
    },
    enableSorting: false,
  },
  {
    accessorKey: 'createdAt',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Created At' />
    ),
    cell: ({ row }) => {
      const date = new Date(row.getValue('createdAt'))
      return (
        <div className='flex w-[140px] items-center'>
          <span>{date.toLocaleDateString()}</span>
        </div>
      )
    },
    enableSorting: true,
  },
  {
    accessorKey: 'updatedAt',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Updated At' />
    ),
    cell: ({ row }) => {
      const date = new Date(row.getValue('updatedAt'))
      return (
        <div className='flex w-[140px] items-center'>
          <span>{date.toLocaleDateString()}</span>
        </div>
      )
    },
    enableSorting: true,
  },
  {
    id: 'actions',
    cell: ({ row }) => <DataTableRowActions row={row} />,
  },
]
