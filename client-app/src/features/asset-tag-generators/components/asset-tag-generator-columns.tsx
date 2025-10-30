import { ColumnDef } from "@tanstack/react-table";
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
          <div className="text-xs text-muted-foreground">{row.original.assetCategoryCode}</div>
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
          <div className="text-xs text-muted-foreground">{row.original.vendorCode}</div>
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
          <div className="text-xs text-muted-foreground">{row.original.bankCode}</div>
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
