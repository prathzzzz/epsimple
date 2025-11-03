import { useState, useEffect } from 'react'
import {
  type SortingState,
  type VisibilityState,
  flexRender,
  getCoreRowModel,
  getFacetedRowModel,
  getFacetedUniqueValues,
  getFilteredRowModel,
  getSortedRowModel,
  useReactTable,
} from '@tanstack/react-table'
import { useQuery } from '@tanstack/react-query'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { DataTablePagination, DataTableToolbar } from '@/components/data-table'
import { type Bank } from '../data/schema'
import { banksColumns as columns } from './banks-columns'
import { getAllBanks, searchBanks } from '@/features/banks/api/banks-api'
import { Loader2 } from 'lucide-react'

type BanksTableProps = {
  page: number
  pageSize: number
  onPageChange: (page: number) => void
  onPageSizeChange: (pageSize: number) => void
}

export function BanksTable({
  page,
  pageSize,
  onPageChange,
  onPageSizeChange,
}: BanksTableProps) {
  const [sorting, setSorting] = useState<SortingState>([])
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({
    'Created At': false, // Hide by default
  })
  const [globalFilter, setGlobalFilter] = useState('')

  // Fetch data using TanStack Query with real-time search
  const { data: response, isLoading, isError } = useQuery({
    queryKey: ['banks', page - 1, pageSize, globalFilter],
    queryFn: async () => {
      if (globalFilter && globalFilter.trim()) {
        return await searchBanks(globalFilter, page - 1, pageSize, 'id', 'DESC')
      }
      return await getAllBanks(page - 1, pageSize, 'id', 'DESC')
    },
    placeholderData: (previousData) => previousData,
  })

  const banks: Bank[] = response?.data?.content || []
  const totalPages = response?.data?.totalPages || 0

  const table = useReactTable({
    data: banks,
    columns,
    pageCount: totalPages,
    state: {
      sorting,
      columnVisibility,
      globalFilter,
      pagination: {
        pageIndex: page - 1,
        pageSize,
      },
    },
    onSortingChange: setSorting,
    onColumnVisibilityChange: setColumnVisibility,
    onGlobalFilterChange: setGlobalFilter,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFacetedRowModel: getFacetedRowModel(),
    getFacetedUniqueValues: getFacetedUniqueValues(),
    manualPagination: true,
    onPaginationChange: (updater) => {
      if (typeof updater === 'function') {
        const newState = updater({ pageIndex: page - 1, pageSize })
        onPageChange(newState.pageIndex + 1)
        onPageSizeChange(newState.pageSize)
      }
    },
  })

  // Reset to page 1 when search filter changes
  useEffect(() => {
    if (globalFilter) {
      onPageChange(1)
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [globalFilter])

  if (isLoading) {
    return (
      <div className='flex h-96 items-center justify-center'>
        <Loader2 className='size-8 animate-spin text-muted-foreground' />
      </div>
    )
  }

  if (isError) {
    return (
      <div className='flex h-96 items-center justify-center'>
        <p className='text-destructive'>Error loading banks. Please try again.</p>
      </div>
    )
  }

  return (
    <div className='space-y-4'>
      <DataTableToolbar
        table={table}
        searchPlaceholder='Search by bank name...'
      />
      <div className='overflow-hidden rounded-md border'>
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id} colSpan={header.colSpan}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                    </TableHead>
                  )
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && 'selected'}
                >
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={columns.length}
                  className='h-24 text-center'
                >
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <DataTablePagination table={table} />
    </div>
  )
}
