import { ColumnDef } from "@tanstack/react-table";
import { ExpendituresVoucher } from "../api/schema";
import { DataTableColumnHeader } from "@/components/data-table";
import { format } from "date-fns";

export const columns: ColumnDef<ExpendituresVoucher>[] = [
  {
    accessorKey: "costItemFor",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Cost Item" />
    ),
  },
  {
    accessorKey: "costCategoryName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Category" />
    ),
  },
  {
    accessorKey: "voucherNumber",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Voucher Number" />
    ),
  },
  {
    accessorKey: "voucherDate",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Voucher Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("voucherDate") as string;
      return date ? format(new Date(date), "MMM dd, yyyy") : "-";
    },
  },
  {
    accessorKey: "projectName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Project" />
    ),
  },
  {
    accessorKey: "payeeName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Payee" />
    ),
  },
  {
    accessorKey: "incurredDate",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Incurred Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("incurredDate") as string | undefined;
      return date ? format(new Date(date), "MMM dd, yyyy") : "-";
    },
  },
];
