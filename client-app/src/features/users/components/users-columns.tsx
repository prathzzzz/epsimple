import { type ColumnDef } from '@tanstack/react-table'
import { cn } from '@/lib/utils'
import { Badge } from '@/components/ui/badge'
import { DataTableColumnHeader } from '@/components/data-table'
import { LongText } from '@/components/long-text'
import { roles } from '../data/data'
import { type User } from '../data/schema'
import { DataTableRowActions } from './data-table-row-actions'

export const usersColumns: ColumnDef<User>[] = [
  {
    id: 'fullName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Name' />
    ),
    cell: ({ row }) => {
      // API uses 'name', mock uses 'firstName' + 'lastName'
      const fullName = row.original.name || 
                      `${row.original.firstName || ''} ${row.original.lastName || ''}`.trim()
      return <LongText className='max-w-36'>{fullName}</LongText>
    },
    meta: {
      className: cn(
        'drop-shadow-[0_1px_2px_rgb(0_0_0_/_0.1)] dark:drop-shadow-[0_1px_2px_rgb(255_255_255_/_0.1)]',
        'sticky start-0 @4xl/content:table-cell @4xl/content:drop-shadow-none'
      ),
    },
    enableHiding: false,
  },
  {
    accessorKey: 'email',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Email' />
    ),
    cell: ({ row }) => (
      <div className='w-fit text-nowrap'>{row.getValue('email')}</div>
    ),
  },
  {
    accessorKey: 'role',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Roles' />
    ),
    cell: ({ row }) => {
      // API uses 'roles' array, mock uses 'role' string
      const userRoles = row.original.roles || (row.original.role ? [{ name: row.original.role }] : [])

      if (!userRoles || userRoles.length === 0) {
        return <span className='text-xs text-muted-foreground'>No roles</span>
      }

      return (
        <div className='flex flex-wrap gap-1.5'>
          {userRoles.map((role, index) => {
            const roleName = typeof role === 'string' ? role : role.name
            const userType = roles.find(({ value }) => value === roleName)
            
            return (
              <Badge 
                key={index} 
                variant='secondary' 
                className='capitalize text-xs font-normal'
              >
                {userType?.icon && (
                  <userType.icon size={12} className='mr-1' />
                )}
                {roleName}
              </Badge>
            )
          })}
        </div>
      )
    },
    enableSorting: false,
    enableHiding: false,
  },
  {
    id: 'actions',
    cell: DataTableRowActions,
  },
]
