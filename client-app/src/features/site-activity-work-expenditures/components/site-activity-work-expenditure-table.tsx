import { useEffect, useState } from 'react';
import {
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
import { siteActivityWorkExpenditureApi } from '../api/site-activity-work-expenditure-api';
import { useSiteActivityWorkExpenditure } from '../context/site-activity-work-expenditure-provider';
import { siteActivityWorkExpenditureColumns } from './site-activity-work-expenditure-columns';

export function SiteActivityWorkExpenditureTable() {
  const { globalFilter, setGlobalFilter, siteId } = useSiteActivityWorkExpenditure();
  const [rowSelection, setRowSelection] = useState({});
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [sorting, setSorting] = useState<SortingState>([]);
  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });

  const hasSearch = globalFilter && globalFilter.trim() !== '';
  
  // Use site-specific API when siteId is provided
  const { data: siteData, isLoading: isSiteLoading } = siteActivityWorkExpenditureApi.useGetBySiteId(
    siteId || 0,
    {
      page: pagination.pageIndex,
      size: pagination.pageSize,
      sortBy: sorting.length > 0 ? sorting[0].id : 'id',
      sortOrder: sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC',
    }
  );
  
  const { data: searchData, isLoading: isSearchLoading } = siteActivityWorkExpenditureApi.useSearch(globalFilter);

  const { data: allData, isLoading: isAllLoading } = siteActivityWorkExpenditureApi.useGetAll({
    page: pagination.pageIndex,
    size: pagination.pageSize,
    sortBy: sorting.length > 0 ? sorting[0].id : 'id',
    sortOrder: sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC',
  });

  // Use siteData if siteId is provided, otherwise use search or all data
  const data = siteId ? siteData : (hasSearch ? searchData : allData);
  const isLoading = siteId ? isSiteLoading : (hasSearch ? isSearchLoading : isAllLoading);
  
  const expenditures = data?.content || [];
  const totalPages = data?.totalPages || 0;

  useEffect(() => {
    setPagination((prev) => ({ ...prev, pageIndex: 0 }));
  }, [globalFilter]);

  const table = useReactTable({
    data: expenditures,
    columns: siteActivityWorkExpenditureColumns,
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
            {isLoading ? (
              <TableRow>
                <TableCell
                  colSpan={siteActivityWorkExpenditureColumns.length + 1}
                  className="h-24 text-center"
                >
                  Loading...
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
                  colSpan={siteActivityWorkExpenditureColumns.length + 1}
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
