import type { ColumnDef } from "@tanstack/react-table";
import { format } from "date-fns";
import { DataTableColumnHeader } from "@/components/data-table";
import { LongText } from "@/components/long-text";
import type { PaymentDetails } from "../api/schema";

export const paymentDetailsColumns: ColumnDef<PaymentDetails>[] = [
  {
    accessorKey: "paymentDate",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Payment Date" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("paymentDate") as string;
      return <div className="font-medium">{format(new Date(date), "PP")}</div>;
    },
  },
  {
    accessorKey: "paymentMethodName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Payment Method" />
    ),
    cell: ({ row }) => (
      <div>{row.getValue("paymentMethodName")}</div>
    ),
  },
  {
    accessorKey: "paymentAmount",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Amount" />
    ),
    cell: ({ row }) => {
      const amount = row.getValue("paymentAmount") as number;
      return <div className="font-semibold">₹{amount.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</div>;
    },
  },
  {
    accessorKey: "beneficiaryName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Beneficiary" />
    ),
    cell: ({ row }) => {
      const name = row.getValue("beneficiaryName") as string | null;
      return name ? (
        <div>{name}</div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "transactionNumber",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Transaction #" />
    ),
    cell: ({ row }) => {
      const txn = row.getValue("transactionNumber") as string | null;
      return txn ? (
        <div className="font-mono text-sm">{txn}</div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "vpa",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="VPA" />
    ),
    cell: ({ row }) => {
      const vpa = row.getValue("vpa") as string | null;
      return vpa ? (
        <div className="font-mono text-sm">{vpa}</div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "paymentRemarks",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Remarks" />
    ),
    cell: ({ row }) => {
      const remarks = row.getValue("paymentRemarks") as string | null;
      return remarks ? (
        <LongText>{remarks}</LongText>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "createdAt",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Created At" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("createdAt") as string;
      return <div className="text-sm">{format(new Date(date), "PPp")}</div>;
    },
  },
];
