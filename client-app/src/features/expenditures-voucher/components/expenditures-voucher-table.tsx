import React, { useEffect, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  type ColumnDef,
  type ColumnFiltersState,
  flexRender,
  getCoreRowModel,
  getFacetedRowModel,
  getFacetedUniqueValues,
  getFilteredRowModel,
  getSortedRowModel,
  type SortingState,
  useReactTable,
  type VisibilityState,
} from '@tanstack/react-table';
import { type ExpendituresVoucher } from '../api/schema';
import { expendituresVoucherApi } from '../api/expenditures-voucher-api';
import { useExpendituresVoucherContext } from '../context/expenditures-voucher-provider';
import { DataTableRowActions } from './data-table-row-actions';
import { DataTablePagination, DataTableToolbar } from '@/components/data-table';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';

interface ExpendituresVoucherTableProps {
  columns: ColumnDef<ExpendituresVoucher>[];
}

export function ExpendituresVoucherTable({ columns }: ExpendituresVoucherTableProps) {
  const { globalFilter, setGlobalFilter } = useExpendituresVoucherContext();
  const [rowSelection, setRowSelection] = useState({});
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [sorting, setSorting] = useState<SortingState>([{ id: 'id', desc: true }]);
  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });

  const { data, isLoading } = useQuery({
    queryKey: [
      'expenditures-vouchers',
      pagination.pageIndex,
      pagination.pageSize,
      globalFilter,
      sorting.length,
      sorting[0]?.id,
      sorting[0]?.desc,
    ],
    queryFn: async () => {
      const sortBy = sorting.length > 0 ? sorting[0].id : 'id';
      const sortDirection = sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC';

      if (globalFilter && globalFilter.trim() !== '') {
        return await expendituresVoucherApi.search(
          globalFilter,
          pagination.pageIndex,
          pagination.pageSize,
          sortBy,
          sortDirection
        );
      }

      return await expendituresVoucherApi.getAll(
        pagination.pageIndex,
        pagination.pageSize,
        sortBy,
        sortDirection
      );
    },
  });

  const expenditures = (data?.data?.content || []) as ExpendituresVoucher[];
  const totalPages = data?.data?.page?.totalPages || 1;

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
        cell: ({ row }) => <DataTableRowActions row={row as any} />,
      },
    ],
    [columns]
  );

  const table = useReactTable({
    data: expenditures,
    columns: columnsWithActions as ColumnDef<ExpendituresVoucher>[],
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
    manualFiltering: true,
    manualSorting: true,
  });

  return (
    <div className="space-y-4">
      <DataTableToolbar
        table={table}
        searchPlaceholder="Search expenditures vouchers..."
      />
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
            {isLoading ? (
              <TableRow>
                <TableCell
                  colSpan={columns.length + 1}
                  className="h-24 text-center"
                >
                  Loading...
                </TableCell>
              </TableRow>
            ) : table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && "selected"}
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
                  colSpan={columns.length + 1}
                  className="h-24 text-center"
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
  );
}
