import React, { useEffect, useState } from 'react';
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
import { useQuery } from '@tanstack/react-query';

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';

import { DataTablePagination, DataTableToolbar } from '@/components/data-table';
import { vendorCategoriesApi } from '../api/vendor-categories-api';
import { DataTableRowActions } from './data-table-row-actions';

interface VendorCategoriesTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[];
}

export function VendorCategoriesTable<TData, TValue>({
  columns,
}: VendorCategoriesTableProps<TData, TValue>) {
  const [globalFilter, setGlobalFilter] = useState('');
  const [rowSelection, setRowSelection] = useState({});
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({
    createdAt: false,
    updatedAt: false,
  });
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [sorting, setSorting] = useState<SortingState>([]);
  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });

  const { data, isLoading } = useQuery({
    queryKey: ['vendor-categories', pagination.pageIndex, pagination.pageSize, globalFilter, sorting.length, sorting[0]?.id, sorting[0]?.desc],
    queryFn: async () => {
      const sortBy = sorting.length > 0 ? sorting[0].id : 'id';
      const sortDirection = sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC';

      if (globalFilter && globalFilter.trim() !== '') {
        return await vendorCategoriesApi.search(
          globalFilter,
          pagination.pageIndex,
          pagination.pageSize,
          sortBy,
          sortDirection
        );
      }

      return await vendorCategoriesApi.getAll(
        pagination.pageIndex,
        pagination.pageSize,
        sortBy,
        sortDirection
      );
    },
  });

  const vendorCategories = (data?.data?.content || []) as TData[];
  const totalPages = data?.data?.totalPages || 0;

  // Reset to first page when search query changes
  useEffect(() => {
    setPagination((prev) => ({ ...prev, pageIndex: 0 }));
  }, [globalFilter]);

  // Add actions column
  const columnsWithActions = React.useMemo(
    () => [
      ...columns,
      {
        id: 'actions',
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        cell: ({ row }: { row: any }) => <DataTableRowActions row={row} />,
      },
    ],
    [columns]
  );

  const table = useReactTable({
    data: vendorCategories,
    columns: columnsWithActions as ColumnDef<TData, TValue>[],
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
    onColumnFiltersChange: setColumnFilters,
    onColumnVisibilityChange: setColumnVisibility,
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
      <DataTableToolbar table={table} searchPlaceholder="Search vendor categories..." />
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
                        : flexRender(header.column.columnDef.header, header.getContext())}
                    </TableHead>
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={columns.length + 1} className="h-24 text-center">
                  Loading...
                </TableCell>
              </TableRow>
            ) : table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow key={row.id} data-state={row.getIsSelected() && 'selected'}>
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length + 1} className="h-24 text-center">
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <DataTablePagination table={table} />
    </div>
  );
}
