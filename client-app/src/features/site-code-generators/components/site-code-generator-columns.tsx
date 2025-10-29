import { ColumnDef } from "@tanstack/react-table";
import type { SiteCodeGenerator } from "../api/schema";
import { SiteCodeGeneratorRowActions } from "./site-code-generator-row-actions";

export const siteCodeGeneratorColumns: ColumnDef<SiteCodeGenerator>[] = [
  {
    accessorKey: "projectName",
    header: "Project",
    cell: ({ row }) => (
      <div>
        <div className="font-medium">{row.original.projectName}</div>
        {row.original.projectCode && (
          <div className="text-xs text-muted-foreground">{row.original.projectCode}</div>
        )}
      </div>
    ),
  },
  {
    accessorKey: "stateName",
    header: "State",
    cell: ({ row }) => (
      <div>
        <div className="font-medium">{row.original.stateName}</div>
        <div className="text-xs text-muted-foreground">{row.original.stateCode}</div>
      </div>
    ),
  },
  {
    accessorKey: "maxSeqDigit",
    header: "Max Digits",
    cell: ({ row }) => <div className="text-center">{row.getValue("maxSeqDigit")}</div>,
  },
  {
    accessorKey: "runningSeq",
    header: "Current Seq",
    cell: ({ row }) => (
      <div className="text-center font-mono text-sm">{row.getValue("runningSeq")}</div>
    ),
  },
  {
    id: "actions",
    cell: ({ row }) => <SiteCodeGeneratorRowActions row={row} />,
  },
];
