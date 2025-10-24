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
          <span className="max-w-[400px] truncate font-medium">
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
          <span className="max-w-[200px] truncate font-mono text-sm font-semibold">
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
          <span className="max-w-[250px] truncate">
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
          <span className="max-w-[150px] truncate font-mono text-sm">
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
