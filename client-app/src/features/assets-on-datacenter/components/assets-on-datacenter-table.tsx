import { useEffect, useState } from 'react';
import {
  type ColumnDef,
  type ColumnFiltersState,
  type SortingState,
  type VisibilityState,
  flexRender,
  getCoreRowModel,
  getFacetedRowModel,
  getFacetedUniqueValues,
  getFilteredRowModel,
  getSortedRowModel,
  useReactTable,
} from '@tanstack/react-table';

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';

import { DataTablePagination, DataTableToolbar } from '@/components/data-table';
import { assetsOnDatacenterApi } from '../api/assets-on-datacenter-api';
import { useAssetsOnDatacenter } from '../context/assets-on-datacenter-provider';

interface AssetsOnDatacenterTableProps<TData, TValue> {
  readonly columns: ColumnDef<TData, TValue>[];
}

export function AssetsOnDatacenterTable<TData, TValue>({
  columns,
}: AssetsOnDatacenterTableProps<TData, TValue>) {
  const { globalFilter, setGlobalFilter } = useAssetsOnDatacenter();
  const [rowSelection, setRowSelection] = useState({});
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [sorting, setSorting] = useState<SortingState>([]);
  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });

  const hasSearch = globalFilter && globalFilter.trim() !== '';
  
  const { data: searchData, isLoading: isSearchLoading } = assetsOnDatacenterApi.useSearch({
    searchTerm: globalFilter,
    page: pagination.pageIndex,
    size: pagination.pageSize,
    sortBy: sorting.length > 0 ? sorting[0].id : 'id',
    sortDirection: sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC',
  });

  const { data: allData, isLoading: isAllLoading } = assetsOnDatacenterApi.useGetAll({
    page: pagination.pageIndex,
    size: pagination.pageSize,
    sortBy: sorting.length > 0 ? sorting[0].id : 'id',
    sortDirection: sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC',
  });

  const data = hasSearch ? searchData : allData;
  const isLoading = hasSearch ? isSearchLoading : isAllLoading;
  
  const placements = (data?.content || []) as TData[];
  const totalPages = data?.totalPages || 0;

  useEffect(() => {
    setPagination((prev) => ({ ...prev, pageIndex: 0 }));
  }, [globalFilter]);

  const table = useReactTable({
    data: placements,
    columns,
    pageCount: totalPages,
    state: {
      sorting,
      columnVisibility,
      rowSelection,
      columnFilters,
      pagination,
      globalFilter,
    },
    enableRowSelection: true,
    onRowSelectionChange: setRowSelection,
    onSortingChange: setSorting,
    onColumnVisibilityChange: setColumnVisibility,
    onColumnFiltersChange: setColumnFilters,
    onPaginationChange: setPagination,
    onGlobalFilterChange: setGlobalFilter,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFacetedRowModel: getFacetedRowModel(),
    getFacetedUniqueValues: getFacetedUniqueValues(),
    manualPagination: true,
    manualSorting: true,
  });

  return (
    <div className="space-y-4">
      <DataTableToolbar table={table} />
      <div className="rounded-md border">
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
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {(() => {
              if (isLoading) {
                return (
                  <TableRow>
                    <TableCell
                      colSpan={columns.length + 1}
                      className="h-24 text-center"
                    >
                      Loading...
                    </TableCell>
                  </TableRow>
                );
              }
              if (table.getRowModel().rows?.length) {
                return table.getRowModel().rows.map((row) => (
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
                ));
              }
              return (
                <TableRow>
                  <TableCell
                    colSpan={columns.length + 1}
                    className="h-24 text-center"
                  >
                    No results.
                  </TableCell>
                </TableRow>
              );
            })()}
          </TableBody>
        </Table>
      </div>
      <DataTablePagination table={table} />
    </div>
  );
}
