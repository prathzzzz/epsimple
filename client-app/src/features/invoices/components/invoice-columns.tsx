import type { ColumnDef } from "@tanstack/react-table";
import { format } from "date-fns";
import { DataTableColumnHeader } from "@/components/data-table";
import type { Invoice } from "../api/schema";

export const invoiceColumns: ColumnDef<Invoice>[] = [
  {
    accessorKey: "invoiceNumber",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Invoice #" />
    ),
    cell: ({ row }) => (
      <div className="font-medium">{row.getValue("invoiceNumber")}</div>
    ),
  },
  {
    accessorKey: "invoiceDate",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Invoice Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("invoiceDate") as string;
      return <div>{format(new Date(date), "PP")}</div>;
    },
  },
  {
    accessorKey: "payeeName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Payee" />
    ),
    cell: ({ row }) => (
      <div>{row.getValue("payeeName")}</div>
    ),
  },
  {
    accessorKey: "vendorName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Vendor" />
    ),
    cell: ({ row }) => {
      const vendor = row.getValue("vendorName") as string | null;
      return vendor ? (
        <div>{vendor}</div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "totalInvoiceValue",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Total Value" />
    ),
    cell: ({ row }) => {
      const amount = row.getValue("totalInvoiceValue") as number | null;
      return amount ? (
        <div className="font-semibold">
          ₹{amount.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "paymentStatus",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Payment Status" />
    ),
    cell: ({ row }) => {
      const status = row.getValue("paymentStatus") as string | null;
      return status ? (
        <div className="capitalize">{status.toLowerCase()}</div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "paymentDueDate",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Due Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("paymentDueDate") as string | null;
      return date ? (
        <div>{format(new Date(date), "PP")}</div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
];
