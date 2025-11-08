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
          <div className='flex space-x-2 mt-1'>
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {row.original.projectCode}
            </span>
          </div>
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
        <div className='flex space-x-2 mt-1'>
          <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
            {row.original.stateCode}
          </span>
        </div>
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
