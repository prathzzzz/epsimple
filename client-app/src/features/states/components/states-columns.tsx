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
          <span className="inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30">
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
          {row.original.stateCodeAlt ? (
            <span className="inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30">
              {row.original.stateCodeAlt}
            </span>
          ) : (
            <span className="text-muted-foreground">-</span>
          )}
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
