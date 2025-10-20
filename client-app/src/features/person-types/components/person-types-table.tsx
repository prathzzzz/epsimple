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
import { type PersonType } from '../data/schema'
import { personTypesColumns } from './person-types-columns'
import { personTypesApi } from '@/lib/person-types-api'
import { Loader2 } from 'lucide-react'

type PersonTypesTableProps = {
  page: number
  pageSize: number
  onPageChange: (page: number) => void
  onPageSizeChange: (pageSize: number) => void
}

export function PersonTypesTable({
  page,
  pageSize,
  onPageChange,
  onPageSizeChange,
}: PersonTypesTableProps) {
  const [sorting, setSorting] = useState<SortingState>([])
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({
    createdAt: false, // Hide by default
    updatedAt: false, // Hide by default
  })
  const [globalFilter, setGlobalFilter] = useState('')

  // Fetch data using TanStack Query with real-time search
  const { data: response, isLoading, isError } = useQuery({
    queryKey: ['person-types', page - 1, pageSize, globalFilter],
    queryFn: async () => {
      const sortBy = sorting[0]?.id || 'id'
      const sortDirection = sorting[0]?.desc ? 'DESC' : 'ASC'

      if (globalFilter && globalFilter.trim() !== '') {
        return await personTypesApi.search(globalFilter, {
          page: page - 1,
          size: pageSize,
          sortBy,
          sortDirection,
        })
      }

      return await personTypesApi.getAll({
        page: page - 1,
        size: pageSize,
        sortBy,
        sortDirection,
      })
    },
    placeholderData: (previousData) => previousData,
  })

  const personTypes: PersonType[] = response?.data?.content || []
  const totalPages = response?.data?.totalPages || 0

  const table = useReactTable({
    data: personTypes,
    columns: personTypesColumns,
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
            Error loading person types
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
      <DataTableToolbar table={table} />
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <TableHead key={header.id} colSpan={header.colSpan}>
                    {header.isPlaceholder
                      ? null
                      : flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                  </TableHead>
                ))}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={personTypesColumns.length} className="h-24 text-center">
                  <Loader2 className='mx-auto size-6 animate-spin' />
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
                <TableCell colSpan={personTypesColumns.length} className="h-24 text-center">
                  No person types found.
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
