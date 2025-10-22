import { ColumnDef } from "@tanstack/react-table";
import { format } from "date-fns";
import { DataTableColumnHeader } from "@/components/data-table";
import { LongText } from "@/components/long-text";
import type { GenericStatusType } from "../api/schema";

export const genericStatusTypeColumns: ColumnDef<GenericStatusType>[] = [
  {
    accessorKey: "statusName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Status Name" />
    ),
    cell: ({ row }) => (
      <div className="font-medium">{row.getValue("statusName")}</div>
    ),
  },
  {
    accessorKey: "statusCode",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Status Code" />
    ),
    cell: ({ row }) => {
      const code = row.getValue("statusCode") as string | null;
      return code ? (
        <div className="font-mono text-sm">{code}</div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "description",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Description" />
    ),
    cell: ({ row }) => {
      const description = row.getValue("description") as string | null;
      return description ? (
        <LongText>{description}</LongText>
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
  {
    accessorKey: "updatedAt",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Updated At" />
    ),
    cell: ({ row }) => {
      const date = row.getValue("updatedAt") as string;
      return <div className="text-sm">{format(new Date(date), "PPp")}</div>;
    },
  },
];
