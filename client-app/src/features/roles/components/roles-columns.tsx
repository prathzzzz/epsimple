import { type ColumnDef } from '@tanstack/react-table'
import { Shield } from 'lucide-react'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { DataTableColumnHeader } from '@/components/data-table'
import { LongText } from '@/components/long-text'
import { type RoleDTO } from '@/lib/roles-api'
import { DataTableRowActions } from './data-table-row-actions'

export const rolesColumns: ColumnDef<RoleDTO>[] = [
  {
    accessorKey: 'name',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Role Name' />
    ),
    cell: ({ row }) => (
      <div className='flex items-center gap-2'>
        <Shield className='h-4 w-4 text-muted-foreground' />
        <LongText className='max-w-48 font-medium ps-1'>
          {row.getValue('name')}
        </LongText>
      </div>
    ),
    meta: {
      className: cn(
        'drop-shadow-[0_1px_2px_rgb(0_0_0_/_0.1)] dark:drop-shadow-[0_1px_2px_rgb(255_255_255_/_0.1)]',
        'sticky start-0 @4xl/content:table-cell @4xl/content:drop-shadow-none'
      ),
    },
    enableHiding: false,
  },
  {
    accessorKey: 'description',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Description' />
    ),
    cell: ({ row }) => {
      const description = row.getValue('description') as string | null
      return (
        <LongText className='max-w-64'>
          {description || <span className='text-muted-foreground'>—</span>}
        </LongText>
      )
    },
    enableSorting: false,
  },
  {
    id: 'permissionCount',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Permissions' />
    ),
    cell: ({ row }) => {
      const permissions = row.original.permissions || []
      return (
        <Badge variant='secondary' className='font-normal'>
          {permissions.length} {permissions.length === 1 ? 'permission' : 'permissions'}
        </Badge>
      )
    },
    enableSorting: false,
  },
  {
    accessorKey: 'isActive',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Status' />
    ),
    cell: ({ row }) => {
      const isActive = row.getValue('isActive') as boolean
      return (
        <Badge
          variant='outline'
          className={cn(
            'capitalize',
            isActive
              ? 'border-green-500/50 bg-green-500/10 text-green-700 dark:text-green-400'
              : 'border-gray-500/50 bg-gray-500/10 text-gray-700 dark:text-gray-400'
          )}
        >
          {isActive ? 'Active' : 'Inactive'}
        </Badge>
      )
    },
    filterFn: (row, id, value) => {
      return value.includes(row.getValue(id) ? 'active' : 'inactive')
    },
    enableSorting: false,
    enableHiding: false,
  },
  {
    accessorKey: 'isSystemRole',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Type' />
    ),
    cell: ({ row }) => {
      const isSystem = row.getValue('isSystemRole') as boolean
      return isSystem ? (
        <Badge variant='outline' className='border-blue-500/50 bg-blue-500/10 text-blue-700 dark:text-blue-400'>
          System
        </Badge>
      ) : (
        <Badge variant='outline'>Custom</Badge>
      )
    },
    enableSorting: false,
  },
  {
    id: 'actions',
    cell: DataTableRowActions,
  },
]
