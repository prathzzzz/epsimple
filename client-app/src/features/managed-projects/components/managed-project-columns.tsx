import type { ColumnDef } from "@tanstack/react-table";
import { DataTableColumnHeader } from "@/components/data-table";
import type { ManagedProject } from "../api/schema";

export const managedProjectColumns: ColumnDef<ManagedProject>[] = [
  {
    accessorKey: "projectName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Project Name" />
    ),
    cell: ({ row }) => {
      return <div className="font-medium">{row.getValue("projectName")}</div>;
    },
  },
  {
    accessorKey: "projectCode",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Project Code" />
    ),
    cell: ({ row }) => {
      const code = row.getValue("projectCode") as string | undefined;
      return (
        <div className='flex space-x-2'>
          {code ? (
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {code}
            </span>
          ) : (
            <span className='text-muted-foreground'>—</span>
          )}
        </div>
      );
    },
  },
  {
    accessorKey: "projectType",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Project Type" />
    ),
    cell: ({ row }) => {
      const type = row.getValue("projectType") as string | undefined;
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
    accessorKey: "bankName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Bank" />
    ),
    cell: ({ row }) => {
      return <div>{row.getValue("bankName")}</div>;
    },
  },
  {
    accessorKey: "projectDescription",
    header: "Description",
    cell: ({ row }) => {
      const description = row.getValue("projectDescription") as string | undefined;
      return (
        <div className="max-w-[300px] truncate" title={description}>
          {description || "—"}
        </div>
      );
    },
  },
];
