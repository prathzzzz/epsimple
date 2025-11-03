import { ColumnDef } from "@tanstack/react-table";
import { ExpendituresInvoice } from "../api/schema";
import { DataTableColumnHeader } from "@/components/data-table";
import { format } from "date-fns";

export const columns: ColumnDef<ExpendituresInvoice>[] = [
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
    accessorKey: "invoiceNumber",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Invoice Number" />
    ),
  },
  {
    accessorKey: "invoiceDate",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Invoice Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("invoiceDate") as string;
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
