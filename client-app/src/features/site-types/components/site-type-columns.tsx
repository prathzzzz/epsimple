import { ColumnDef } from "@tanstack/react-table";
import { DataTableColumnHeader } from "@/components/data-table";
import type { SiteType } from "../api/schema";

export const siteTypeColumns: ColumnDef<SiteType>[] = [
  {
    accessorKey: "typeName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Type Name" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
            {row.getValue("typeName")}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: "description",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Description" />
    ),
    cell: ({ row }) => {
      const description = row.getValue("description") as string;
      return (
        <div className="max-w-[500px] truncate">
          {description || "-"}
        </div>
      );
    },
  },
];
