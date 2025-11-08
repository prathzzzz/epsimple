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
      <div className='flex space-x-2'>
        <span className='inline-flex items-center rounded-md bg-green-50 px-2 py-1 text-xs font-medium text-green-700 ring-1 ring-inset ring-green-700/10 dark:bg-green-400/10 dark:text-green-400 dark:ring-green-400/30'>
          {row.getValue("statusName")}
        </span>
      </div>
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
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {code}
          </span>
        </div>
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
