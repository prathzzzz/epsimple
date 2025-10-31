import type { ColumnDef } from "@tanstack/react-table";
import { format } from "date-fns";
import type { Voucher } from "../api/schema";

export const voucherColumns: ColumnDef<Voucher>[] = [
  {
    accessorKey: "voucherNumber",
    header: "Voucher Number",
    cell: ({ row }) => (
      <div className="font-medium">{row.getValue("voucherNumber")}</div>
    ),
  },
  {
    accessorKey: "voucherDate",
    header: "Voucher Date",
    cell: ({ row }) => {
      const date = row.getValue("voucherDate") as string;
      return date ? format(new Date(date), "dd MMM yyyy") : "-";
    },
  },
  {
    accessorKey: "payeeName",
    header: "Payee",
    cell: ({ row }) => row.getValue("payeeName") || "-",
  },
  {
    accessorKey: "orderNumber",
    header: "Order Number",
    cell: ({ row }) => row.getValue("orderNumber") || "-",
  },
  {
    accessorKey: "finalAmount",
    header: "Final Amount",
    cell: ({ row }) => {
      const amount = row.getValue("finalAmount") as number | null;
      return amount != null
        ? new Intl.NumberFormat("en-IN", {
            style: "currency",
            currency: "INR",
          }).format(amount)
        : "-";
    },
  },
  {
    accessorKey: "paymentStatus",
    header: "Payment Status",
    cell: ({ row }) => {
      const status = row.getValue("paymentStatus") as string | null;
      return status ? (
        <span
          className={`inline-flex rounded-full px-2 py-1 text-xs font-semibold ${
            status === "PAID"
              ? "bg-green-100 text-green-800"
              : status === "APPROVED"
                ? "bg-blue-100 text-blue-800"
                : status === "PENDING"
                  ? "bg-yellow-100 text-yellow-800"
                  : "bg-gray-100 text-gray-800"
          }`}
        >
          {status}
        </span>
      ) : (
        "-"
      );
    },
  },
  {
    accessorKey: "paymentDueDate",
    header: "Due Date",
    cell: ({ row }) => {
      const date = row.getValue("paymentDueDate") as string | null;
      return date ? format(new Date(date), "dd MMM yyyy") : "-";
    },
  },
];
