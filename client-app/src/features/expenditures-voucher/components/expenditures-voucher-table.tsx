import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  SortingState,
  useReactTable,
  VisibilityState,
} from '@tanstack/react-table';
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-react';
import { ExpendituresVoucher } from '../api/schema';
import { expendituresVoucherApi } from '../api/expenditures-voucher-api';
import { useExpendituresVoucherContext } from '../context/expenditures-voucher-provider';
import { DataTableRowActions } from './data-table-row-actions';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

interface ExpendituresVoucherTableProps {
  columns: ColumnDef<ExpendituresVoucher>[];
}

export function ExpendituresVoucherTable({ columns }: ExpendituresVoucherTableProps) {
  const { globalFilter, setGlobalFilter } = useExpendituresVoucherContext();
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sorting, setSorting] = useState<SortingState>([{ id: 'id', desc: true }]);
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});

  const hasSearch = globalFilter && globalFilter.trim().length > 0;

  // Query for all data
  const { data: allData, isLoading: isAllLoading } = useQuery({
    queryKey: ['expenditures-vouchers', page, pageSize, sorting[0]?.id, sorting[0]?.desc],
    queryFn: () =>
      expendituresVoucherApi.getAll(
        page,
        pageSize,
        sorting[0]?.id || 'id',
        sorting[0]?.desc ? 'DESC' : 'ASC'
      ),
    enabled: !hasSearch,
  });

  // Query for search data
  const { data: searchData, isLoading: isSearchLoading } = useQuery({
    queryKey: ['expenditures-vouchers', 'search', globalFilter, page, pageSize, sorting[0]?.id, sorting[0]?.desc],
    queryFn: () =>
      expendituresVoucherApi.search(
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

  const columnsWithActions: ColumnDef<ExpendituresVoucher>[] = [
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

  return (
    <div className="space-y-4">
      {/* Search */}
      <div className="flex items-center gap-2">
        <Input
          placeholder="Search expenditures..."
          value={globalFilter}
          onChange={(e) => {
            setGlobalFilter(e.target.value);
            setPage(0);
          }}
          className="max-w-sm"
        />
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
                <TableCell
                  colSpan={columnsWithActions.length}
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
                  colSpan={columnsWithActions.length}
                  className="h-24 text-center"
                >
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-between px-2">
        <div className="flex items-center space-x-2">
          <p className="text-sm font-medium">Rows per page</p>
          <Select
            value={`${pageSize}`}
            onValueChange={(value) => {
              setPageSize(Number(value));
              setPage(0);
            }}
          >
            <SelectTrigger className="h-8 w-[70px]">
              <SelectValue placeholder={pageSize} />
            </SelectTrigger>
            <SelectContent side="top">
              {[10, 20, 30, 40, 50].map((size) => (
                <SelectItem key={size} value={`${size}`}>
                  {size}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
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
}
