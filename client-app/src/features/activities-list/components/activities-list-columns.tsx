import { ColumnDef } from "@tanstack/react-table";
import { format } from "date-fns";
import { DataTableColumnHeader } from "@/components/data-table";
import { LongText } from "@/components/long-text";
import type { ActivitiesList } from "../api/schema";

export const activitiesListColumns: ColumnDef<ActivitiesList>[] = [
  {
    accessorKey: "activityName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Activity Name" />
    ),
    cell: ({ row }) => (
      <div className="font-medium">{row.getValue("activityName")}</div>
    ),
  },
  {
    accessorKey: "activityCategory",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Category" />
    ),
    cell: ({ row }) => {
      const category = row.getValue("activityCategory") as string | null;
      return category ? (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
            {category}
          </span>
        </div>
      ) : (
        <span className="text-muted-foreground">-</span>
      );
    },
  },
  {
    accessorKey: "activityDescription",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Description" />
    ),
    cell: ({ row }) => {
      const description = row.getValue("activityDescription") as string | null;
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
