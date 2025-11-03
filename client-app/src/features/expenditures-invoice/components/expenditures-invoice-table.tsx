import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useExpendituresInvoiceContext } from '../context/expenditures-invoice-provider';
import { expendituresInvoiceApi } from '../api/expenditures-invoice-api';
import {
  ColumnDef,
  SortingState,
  VisibilityState,
  flexRender,
  getCoreRowModel,
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
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight, Search } from 'lucide-react';
import type { ExpendituresInvoice } from '../api/schema';
import { DataTableRowActions } from './data-table-row-actions';

interface ExpendituresInvoiceTableProps {
  columns: ColumnDef<ExpendituresInvoice>[];
}

export const ExpendituresInvoiceTable = ({ columns }: ExpendituresInvoiceTableProps) => {
  const { globalFilter, setGlobalFilter } = useExpendituresInvoiceContext();
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sorting, setSorting] = useState<SortingState>([{ id: 'id', desc: true }]);
  const [searchInput, setSearchInput] = useState('');
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});

  const hasSearch = globalFilter && globalFilter.trim().length > 0;

  // Query for all data
  const { data: allData, isLoading: isAllLoading } = useQuery({
    queryKey: ['expenditures-invoices', page, pageSize, sorting],
    queryFn: () =>
      expendituresInvoiceApi.getAll(
        page,
        pageSize,
        sorting[0]?.id || 'id',
        sorting[0]?.desc ? 'DESC' : 'ASC'
      ),
    enabled: !hasSearch,
  });

  // Query for search data
  const { data: searchData, isLoading: isSearchLoading } = useQuery({
    queryKey: ['expenditures-invoices', 'search', globalFilter, page, pageSize, sorting],
    queryFn: () =>
      expendituresInvoiceApi.search(
        globalFilter,
        page,
        pageSize,
        sorting[0]?.id || 'id',
        sorting[0]?.desc ? 'DESC' : 'ASC'
      ),
    enabled: !!hasSearch,
  });

  const data = hasSearch ? searchData : allData;
  const isLoading = hasSearch ? isSearchLoading : isAllLoading;

  const columnsWithActions: ColumnDef<ExpendituresInvoice>[] = [
    ...columns,
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => <DataTableRowActions row={row} />,
    },
  ];

  const table = useReactTable({
    data: data?.data?.content || [],
    columns: columnsWithActions,
    state: {
      sorting,
      columnVisibility,
    },
    onSortingChange: setSorting,
    onColumnVisibilityChange: setColumnVisibility,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    manualPagination: true,
    manualSorting: true,
    pageCount: data?.data?.page?.totalPages || 0,
  });

  const handleSearch = () => {
    setGlobalFilter(searchInput);
    setPage(0);
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="space-y-4">
      {/* Search */}
      <div className="flex items-center gap-2">
        <Input
          placeholder="Search expenditures..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onKeyPress={handleKeyPress}
          className="max-w-sm"
        />
        <Button onClick={handleSearch} size="icon" variant="outline">
          <Search className="h-4 w-4" />
        </Button>
      </div>

      {/* Table */}
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <TableHead key={header.id}>
                    {header.isPlaceholder
                      ? null
                      : flexRender(header.column.columnDef.header, header.getContext())}
                  </TableHead>
                ))}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={columnsWithActions.length} className="h-24 text-center">
                  Loading...
                </TableCell>
              </TableRow>
            ) : table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow key={row.id}>
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={columnsWithActions.length} className="h-24 text-center">
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      {/* Pagination */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <span className="text-sm text-muted-foreground">Rows per page:</span>
          <select
            value={pageSize}
            onChange={(e) => {
              setPageSize(Number(e.target.value));
              setPage(0);
            }}
            className="h-8 rounded-md border border-input bg-background px-2 text-sm"
          >
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
          </select>
        </div>

        <div className="flex items-center gap-2">
          <span className="text-sm text-muted-foreground">
            Page {page + 1} of {data?.data?.page?.totalPages || 1}
          </span>
          <div className="flex gap-1">
            <Button
              variant="outline"
              size="icon"
              onClick={() => setPage(0)}
              disabled={page === 0 || isLoading}
            >
              <ChevronsLeft className="h-4 w-4" />
            </Button>
            <Button
              variant="outline"
              size="icon"
              onClick={() => setPage((p) => Math.max(0, p - 1))}
              disabled={page === 0 || isLoading}
            >
              <ChevronLeft className="h-4 w-4" />
            </Button>
            <Button
              variant="outline"
              size="icon"
              onClick={() => setPage((p) => p + 1)}
              disabled={!data || page >= (data.data?.page?.totalPages || 1) - 1 || isLoading}
            >
              <ChevronRight className="h-4 w-4" />
            </Button>
            <Button
              variant="outline"
              size="icon"
              onClick={() => setPage((data?.data?.page?.totalPages || 1) - 1)}
              disabled={!data || page >= (data.data?.page?.totalPages || 1) - 1 || isLoading}
            >
              <ChevronsRight className="h-4 w-4" />
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};
