import { ColumnDef } from "@tanstack/react-table";
import { DataTableColumnHeader } from "@/components/data-table";
import type { AssetCategory } from "../api/schema";

export const assetCategoryColumns: ColumnDef<AssetCategory>[] = [
  {
    accessorKey: "categoryName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Category Name" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
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
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {row.getValue("categoryCode")}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: "assetTypeName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Type" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
            {row.getValue("assetTypeName")}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: "assetCodeAlt",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Code Alt" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className='inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-700 ring-1 ring-inset ring-gray-700/10 dark:bg-gray-400/10 dark:text-gray-400 dark:ring-gray-400/30'>
            {row.getValue("assetCodeAlt")}
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
        <div className="max-w-[400px] truncate">
          {description || "-"}
        </div>
      );
    },
  },
];
