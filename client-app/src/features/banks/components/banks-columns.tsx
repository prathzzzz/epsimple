import { type ColumnDef } from '@tanstack/react-table'
import { DataTableColumnHeader } from '@/components/data-table'
import { type Bank } from '../data/schema'
import { DataTableRowActions } from './data-table-row-actions'
import { format } from 'date-fns'

export const banksColumns: ColumnDef<Bank>[] = [
  {
    id: 'Bank Name',
    accessorKey: 'bankName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Bank Name' />
    ),
    cell: ({ row }) => {
      const bankName = row.original.bankName
      const bankLogo = row.original.bankLogo
      
      // Construct the full logo URL if it's a relative path
      const logoUrl = bankLogo && !bankLogo.startsWith('http') 
        ? `${import.meta.env.VITE_API_URL || 'http://localhost:8080'}${bankLogo.startsWith('/') ? '' : '/'}${bankLogo}`
        : bankLogo
      
      return (
        <div className='flex items-center space-x-3'>
          {logoUrl ? (
            <img 
              src={logoUrl} 
              alt={`${bankName} logo`}
              className='h-10 w-10 rounded object-contain bg-white/5 p-1'
              onError={(e) => {
                // Show fallback on error
                const target = e.currentTarget
                target.style.display = 'none'
                const fallback = target.nextElementSibling as HTMLElement
                if (fallback) fallback.style.display = 'flex'
              }}
            />
          ) : null}
          <div 
            className='h-10 w-10 rounded bg-muted flex items-center justify-center text-xs font-medium text-muted-foreground'
            style={{ display: logoUrl ? 'none' : 'flex' }}
          >
            {bankName.slice(0, 2).toUpperCase()}
          </div>
          <span className='max-w-[200px] truncate font-medium sm:max-w-72 md:max-w-[31rem]'>
            {bankName}
          </span>
        </div>
      )
    },
  },
  {
    id: 'RBI Bank Code',
    accessorKey: 'rbiBankCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='RBI Code' />
    ),
    cell: ({ row }) => {
      const code = row.original.rbiBankCode
      return (
        <div className='flex space-x-2'>
          {code ? (
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {code}
            </span>
          ) : (
            <span className='text-muted-foreground'>—</span>
          )}
        </div>
      )
    },
  },
  {
    id: 'EPS Bank Code',
    accessorKey: 'epsBankCode',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='EPS Code' />
    ),
    cell: ({ row }) => {
      const code = row.original.epsBankCode
      return (
        <div className='flex space-x-2'>
          {code ? (
            <span className='inline-flex items-center rounded-md bg-green-50 px-2 py-1 text-xs font-medium text-green-700 ring-1 ring-inset ring-green-700/10 dark:bg-green-400/10 dark:text-green-400 dark:ring-green-400/30'>
              {code}
            </span>
          ) : (
            <span className='text-muted-foreground'>—</span>
          )}
        </div>
      )
    },
  },
  {
    id: 'Bank Code Alt',
    accessorKey: 'bankCodeAlt',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Alt Code' />
    ),
    cell: ({ row }) => {
      const code = row.original.bankCodeAlt
      return (
        <div className='flex space-x-2'>
          {code ? (
            <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
              {code}
            </span>
          ) : (
            <span className='text-muted-foreground'>—</span>
          )}
        </div>
      )
    },
  },
  {
    id: 'Created At',
    accessorKey: 'createdAt',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title='Created At' />
    ),
    cell: ({ row }) => {
      const date = row.original.createdAt
      return (
        <div className='w-[140px]'>
          {format(new Date(date), 'MMM dd, yyyy HH:mm')}
        </div>
      )
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <DataTableRowActions row={row} />,
  },
]
