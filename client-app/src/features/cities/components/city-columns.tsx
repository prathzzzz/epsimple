import { ColumnDef } from "@tanstack/react-table";
import { DataTableColumnHeader } from "@/components/data-table";
import type { City } from "../api/schema";

export const cityColumns: ColumnDef<City>[] = [
  {
    accessorKey: "cityName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="City Name" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[500px] truncate font-medium">
            {row.getValue("cityName")}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: "cityCode",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="City Code" />
    ),
    cell: ({ row }) => {
      const cityCode = row.getValue("cityCode") as string;
      return (
        <div className="flex space-x-2">
          {cityCode ? (
            <span className="inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30">
              {cityCode}
            </span>
          ) : (
            <span className="text-muted-foreground">-</span>
          )}
        </div>
      );
    },
  },
  {
    accessorKey: "stateName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="State" />
    ),
    cell: ({ row }) => {
      return (
        <div className="flex space-x-2">
          <span className="max-w-[300px] truncate">
            {row.getValue("stateName")}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: "stateCode",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="State Code" />
    ),
    cell: ({ row }) => {
      const stateCode = row.getValue("stateCode") as string;
      return (
        <div className="flex space-x-2">
          {stateCode ? (
            <span className="inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30">
              {stateCode}
            </span>
          ) : (
            <span className="text-muted-foreground">-</span>
          )}
        </div>
      );
    },
  },
];
