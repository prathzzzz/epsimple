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
      return <div className="font-medium font-mono">{row.getValue("siteCode")}</div>;
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
      return <div>{category || "—"}</div>;
    },
  },
  {
    accessorKey: "siteTypeName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Type" />
    ),
    cell: ({ row }) => {
      const type = row.getValue("siteTypeName") as string | undefined;
      return <div>{type || "—"}</div>;
    },
  },
  {
    accessorKey: "siteStatusName",
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Status" />
    ),
    cell: ({ row }) => {
      const status = row.getValue("siteStatusName") as string | undefined;
      return <div>{status || "—"}</div>;
    },
  },
];
