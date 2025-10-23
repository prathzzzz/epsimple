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
      return <div>{code || "—"}</div>;
    },
  },
  {
    accessorKey: "projectType",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Project Type" />
    ),
    cell: ({ row }) => {
      const type = row.getValue("projectType") as string | undefined;
      return <div>{type || "—"}</div>;
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
