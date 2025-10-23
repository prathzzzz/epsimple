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
        <div className="max-w-[200px] truncate font-mono text-sm">
          {cityCode || "-"}
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
        <div className="max-w-[150px] truncate font-mono text-sm">
          {stateCode || "-"}
        </div>
      );
    },
  },
];
