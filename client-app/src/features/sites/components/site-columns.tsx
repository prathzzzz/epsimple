import type { ColumnDef } from "@tanstack/react-table";
import { DataTableColumnHeader } from "@/components/data-table";
import type { Site } from "../api/schema";

export const siteColumns: ColumnDef<Site>[] = [
  {
    accessorKey: "siteCode",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Site Code" />
    ),
    cell: ({ row }) => {
      return (
        <div className='flex space-x-2'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {row.getValue("siteCode")}
          </span>
        </div>
      );
    },
  },
  {
    accessorKey: "projectName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Project" />
    ),
    cell: ({ row }) => {
      const project = row.getValue("projectName") as string | undefined;
      return <div>{project || "—"}</div>;
    },
  },
  {
    accessorKey: "locationName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Location" />
    ),
    cell: ({ row }) => {
      return <div>{row.getValue("locationName")}</div>;
    },
  },
  {
    accessorKey: "cityName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="City" />
    ),
    cell: ({ row }) => {
      return <div>{row.getValue("cityName")}</div>;
    },
  },
  {
    accessorKey: "stateName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="State" />
    ),
    cell: ({ row }) => {
      return <div>{row.getValue("stateName")}</div>;
    },
  },
  {
    accessorKey: "siteCategoryName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Category" />
    ),
    cell: ({ row }) => {
      const category = row.getValue("siteCategoryName") as string | undefined;
      return (
        <div className='flex space-x-2'>
          {category ? (
            <span className='inline-flex items-center rounded-md bg-purple-50 px-2 py-1 text-xs font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10 dark:bg-purple-400/10 dark:text-purple-400 dark:ring-purple-400/30'>
              {category}
            </span>
          ) : (
            <span className='text-muted-foreground'>—</span>
          )}
        </div>
      );
    },
  },
  {
    accessorKey: "siteTypeName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Type" />
    ),
    cell: ({ row }) => {
      const type = row.getValue("siteTypeName") as string | undefined;
      return (
        <div className='flex space-x-2'>
          {type ? (
            <span className='inline-flex items-center rounded-md bg-orange-50 px-2 py-1 text-xs font-medium text-orange-700 ring-1 ring-inset ring-orange-700/10 dark:bg-orange-400/10 dark:text-orange-400 dark:ring-orange-400/30'>
              {type}
            </span>
          ) : (
            <span className='text-muted-foreground'>—</span>
          )}
        </div>
      );
    },
  },
  {
    accessorKey: "siteStatusName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Status" />
    ),
    cell: ({ row }) => {
      const status = row.getValue("siteStatusName") as string | undefined;
      return (
        <div className='flex space-x-2'>
          {status ? (
            <span className='inline-flex items-center rounded-md bg-green-50 px-2 py-1 text-xs font-medium text-green-700 ring-1 ring-inset ring-green-700/10 dark:bg-green-400/10 dark:text-green-400 dark:ring-green-400/30'>
              {status}
            </span>
          ) : (
            <span className='text-muted-foreground'>—</span>
          )}
        </div>
      );
    },
  },
];
