import type { ColumnDef } from '@tanstack/react-table'
import type { State } from '../data/schema'
import { DataTableColumnHeader } from '@/components/data-table'
import { DataTableRowActions } from './data-table-row-actions'
import { format } from 'date-fns'

export const columns: ColumnDef<State>[] = [
  {
    id: 'stateName',
    accessorKey: 'stateName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="State Name" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[500px] truncate font-medium">
            {row.original.stateName}
          </span>
        </div>
      )
    },
  },
  {
    id: 'stateCode',
    accessorKey: 'stateCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="State Code" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[200px] truncate">
            {row.original.stateCode}
          </span>
        </div>
      )
    },
  },
  {
    id: 'stateCodeAlt',
    accessorKey: 'stateCodeAlt',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Alternate Code" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[200px] truncate text-muted-foreground">
            {row.original.stateCodeAlt || '-'}
          </span>
        </div>
      )
    },
  },
  {
    id: 'createdAt',
    accessorKey: 'createdAt',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Created At" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[200px] truncate text-muted-foreground">
            {format(new Date(row.original.createdAt), 'dd MMM yyyy, HH:mm')}
          </span>
        </div>
      )
    },
    enableHiding: true,
  },
  {
    id: 'actions',
    cell: ({ row }) => <DataTableRowActions row={row} />,
  },
]
