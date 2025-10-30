import type { ColumnDef } from '@tanstack/react-table'
import { DataTableColumnHeader } from '@/components/data-table'
import { AssetRowActions } from './asset-row-actions'
import type { Asset } from '../api/schema'

export const assetColumns: ColumnDef<Asset>[] = [
  {
    accessorKey: 'assetTagId',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Tag" />
    ),
    cell: ({ row }) => (
      <div className="font-medium">{row.getValue('assetTagId')}</div>
    ),
  },
  {
    accessorKey: 'assetName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Asset Name" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[200px] truncate">{row.getValue('assetName')}</div>
    ),
  },
  {
    accessorKey: 'assetCategoryName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Category" />
    ),
    cell: ({ row }) => (
      <div className="flex flex-col">
        <span className="font-medium">{row.original.assetCategoryName}</span>
        <span className="text-xs text-muted-foreground">
          {row.original.assetCategoryCode}
        </span>
      </div>
    ),
  },
  {
    accessorKey: 'assetTypeName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Type" />
    ),
    cell: ({ row }) => (
      <div className="flex flex-col">
        <span className="font-medium">{row.original.assetTypeName}</span>
        <span className="text-xs text-muted-foreground">
          {row.original.assetTypeCode}
        </span>
      </div>
    ),
  },
  {
    accessorKey: 'vendorName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Vendor" />
    ),
    cell: ({ row }) => (
      <div className="flex flex-col">
        <span className="font-medium">{row.original.vendorName}</span>
        <span className="text-xs text-muted-foreground">
          {row.original.vendorCode}
        </span>
      </div>
    ),
  },
  {
    accessorKey: 'serialNumber',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Serial Number" />
    ),
    cell: ({ row }) => (
      <div className="max-w-[150px] truncate">
        {row.getValue('serialNumber') || '-'}
      </div>
    ),
  },
  {
    accessorKey: 'statusTypeName',
    header: ({ column }) => (
      <DataTableColumnHeader column={column} title="Status" />
    ),
    cell: ({ row }) => (
      <div className="font-medium">{row.getValue('statusTypeName')}</div>
    ),
  },
  {
    id: 'actions',
    cell: ({ row }) => <AssetRowActions row={row} />,
  },
]
