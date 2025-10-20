import { useState } from 'react'
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
import { type State } from '../data/schema'
import { columns } from './states-columns'
import { statesApi } from '@/lib/states-api'
import { Loader2 } from 'lucide-react'

type StatesTableProps = {
  page: number
  pageSize: number
  onPageChange: (page: number) => void
  onPageSizeChange: (pageSize: number) => void
}

export function StatesTable({
  page,
  pageSize,
  onPageChange,
  onPageSizeChange,
}: StatesTableProps) {
  const [sorting, setSorting] = useState<SortingState>([])
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({
    createdAt: false, // Hide by default
  })
  const [globalFilter, setGlobalFilter] = useState('')

  // Fetch data using TanStack Query with real-time search
  const { data: response, isLoading, isError } = useQuery({
    queryKey: ['states', page - 1, pageSize, globalFilter],
    queryFn: async () => {
      return await statesApi.getAll({
        page: page - 1,
        size: pageSize,
        search: globalFilter,
      })
    },
    placeholderData: (previousData) => previousData,
  })

  const states: State[] = response?.data?.content || []
  const totalPages = response?.data?.totalPages || 0

  const table = useReactTable({
    data: states,
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

  if (isError) {
    return (
      <div className="flex h-[450px] items-center justify-center">
        <div className="text-center">
          <p className="text-lg font-semibold text-destructive">
            Error loading states
          </p>
          <p className="text-sm text-muted-foreground">
            Please try again later
          </p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-4">
      <DataTableToolbar
        table={table}
        searchPlaceholder='Search states...'
      />
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
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
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24 text-center">
                  <div className="flex items-center justify-center">
                    <Loader2 className="mr-2 h-6 w-6 animate-spin" />
                    <span>Loading states...</span>
                  </div>
                </TableCell>
              </TableRow>
            ) : table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && 'selected'}
                >
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24 text-center">
                  No states found.
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
