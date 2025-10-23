import { ColumnDef } from "@tanstack/react-table";
import { format } from "date-fns";
import { DataTableColumnHeader } from "@/components/data-table";
import { LongText } from "@/components/long-text";
import type { CostType } from "../api/schema";

export const costTypesColumns: ColumnDef<CostType>[] = [
  {
    accessorKey: "typeName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Type Name" />
    ),
    cell: ({ row }) => (
      <div className="font-medium">{row.getValue("typeName")}</div>
    ),
  },
  {
    accessorKey: "costCategoryName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Cost Category" />
    ),
    cell: ({ row }) => (
      <div>{row.getValue("costCategoryName")}</div>
    ),
  },
  {
    accessorKey: "typeDescription",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Description" />
    ),
    cell: ({ row }) => {
      const description = row.getValue("typeDescription") as string | null;
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
