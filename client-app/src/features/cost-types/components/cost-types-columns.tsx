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
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
          {row.getValue("typeName")}
        </span>
      </div>
    ),
  },
  {
    accessorKey: "costCategoryName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Cost Category" />
    ),
    cell: ({ row }) => (
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
          {row.getValue("costCategoryName")}
        </span>
      </div>
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
