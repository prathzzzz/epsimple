import { type ColumnDef } from "@tanstack/react-table";
import type { AssetTagCodeGenerator } from "../api/schema";
import { AssetTagCodeGeneratorRowActions } from "./asset-tag-generator-row-actions";

export const assetTagCodeGeneratorColumns: ColumnDef<AssetTagCodeGenerator>[] = [
  {
    accessorKey: "assetCategoryName",
    header: "Asset Category",
    cell: ({ row }) => (
      <div>
        <div className="font-medium">{row.original.assetCategoryName}</div>
        {row.original.assetCategoryCode && (
          <div className='flex space-x-2 mt-1'>
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {row.original.assetCategoryCode}
            </span>
          </div>
        )}
      </div>
    ),
  },
  {
    accessorKey: "vendorName",
    header: "Vendor",
    cell: ({ row }) => (
      <div>
        <div className="font-medium">{row.original.vendorName}</div>
        {row.original.vendorCode && (
          <div className='flex space-x-2 mt-1'>
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {row.original.vendorCode}
            </span>
          </div>
        )}
      </div>
    ),
  },
  {
    accessorKey: "bankName",
    header: "Bank",
    cell: ({ row }) => (
      <div>
        <div className="font-medium">{row.original.bankName}</div>
        {row.original.bankCode && (
          <div className='flex space-x-2 mt-1'>
            <span className='inline-flex items-center rounded-md bg-blue-50 px-2 py-1 text-xs font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10 dark:bg-blue-400/10 dark:text-blue-400 dark:ring-blue-400/30'>
              {row.original.bankCode}
            </span>
          </div>
        )}
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
    cell: ({ row }) => <AssetTagCodeGeneratorRowActions row={row} />,
  },
];
