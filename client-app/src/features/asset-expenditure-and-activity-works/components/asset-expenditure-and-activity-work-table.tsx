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
import { assetExpenditureAndActivityWorkApi } from '../api/asset-expenditure-and-activity-work-api';
import { useAssetExpenditureAndActivityWork } from '../context/asset-expenditure-and-activity-work-provider';
import { assetExpenditureAndActivityWorkColumns } from './asset-expenditure-and-activity-work-columns';

export function AssetExpenditureAndActivityWorkTable() {
  const { globalFilter, setGlobalFilter, assetId } = useAssetExpenditureAndActivityWork();
  const [rowSelection, setRowSelection] = useState({});
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [sorting, setSorting] = useState<SortingState>([]);
  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });

  const hasSearch = globalFilter && globalFilter.trim() !== '';
  
  const { data: assetData, isLoading: isAssetLoading } = assetExpenditureAndActivityWorkApi.useGetByAssetId(
    assetId || 0,
    {
      page: pagination.pageIndex,
      size: pagination.pageSize,
      sortBy: sorting.length > 0 ? sorting[0].id : 'id',
      sortOrder: sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC',
    }
  );
  
  const { data: searchData, isLoading: isSearchLoading } = assetExpenditureAndActivityWorkApi.useSearch(globalFilter);

  const { data: allData, isLoading: isAllLoading } = assetExpenditureAndActivityWorkApi.useGetAll({
    page: pagination.pageIndex,
    size: pagination.pageSize,
    sortBy: sorting.length > 0 ? sorting[0].id : 'id',
    sortOrder: sorting.length > 0 && sorting[0].desc ? 'DESC' : 'ASC',
  });

  const data = assetId ? assetData : (hasSearch ? searchData : allData);
  const isLoading = assetId ? isAssetLoading : (hasSearch ? isSearchLoading : isAllLoading);
  
  const expenditures = data?.content || [];
  const totalPages = data?.totalPages || 0;

  useEffect(() => {
    setPagination((prev) => ({ ...prev, pageIndex: 0 }));
  }, [globalFilter]);

  const table = useReactTable({
    data: expenditures,
    columns: assetExpenditureAndActivityWorkColumns,
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
                  colSpan={table.getAllColumns().length}
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
                  colSpan={table.getAllColumns().length}
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
