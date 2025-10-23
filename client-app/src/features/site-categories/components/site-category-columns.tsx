import { ColumnDef } from "@tanstack/react-table";
import { DataTableColumnHeader } from "@/components/data-table";
import type { SiteCategory } from "../api/schema";

export const siteCategoryColumns: ColumnDef<SiteCategory>[] = [
  {
    accessorKey: "categoryName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Category Name" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[500px] truncate font-medium">
            {row.getValue("categoryName")}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: "categoryCode",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Category Code" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[300px] truncate font-mono text-sm">
            {row.getValue("categoryCode")}
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
