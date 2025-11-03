import type { Row } from '@tanstack/react-table'
import { MoreHorizontal, Pencil, Trash, History, MapPin } from 'lucide-react'
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { useAssetContext } from '../context/asset-provider'
import type { Asset } from '../api/schema'

interface AssetRowActionsProps {
  row: Row<Asset>
}

export function AssetRowActions({ row }: AssetRowActionsProps) {
  const asset = row.original
  const {
    setIsDrawerOpen,
    setEditingAsset,
    setIsDeleteDialogOpen,
    setAssetToDelete,
    setIsMovementDialogOpen,
    setAssetForMovement,
    setIsPlacementDialogOpen,
    setAssetForPlacement,
  } = useAssetContext()

  const handleEdit = () => {
    setEditingAsset(asset)
    setIsDrawerOpen(true)
  }

  const handleDelete = () => {
    setAssetToDelete(asset)
    setIsDeleteDialogOpen(true)
  }

  const handleViewMovement = () => {
    setAssetForMovement(asset)
    setIsMovementDialogOpen(true)
  }

  const handlePlaceAsset = () => {
    setAssetForPlacement(asset)
    setIsPlacementDialogOpen(true)
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="flex h-8 w-8 p-0 data-[state=open]:bg-muted"
        >
          <MoreHorizontal className="h-4 w-4" />
          <span className="sr-only">Open menu</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-[180px]">
        <DropdownMenuItem onClick={handlePlaceAsset}>
          <MapPin className="mr-2 h-4 w-4" />
          Place Asset
        </DropdownMenuItem>
        <DropdownMenuItem onClick={handleViewMovement}>
          <History className="mr-2 h-4 w-4" />
          Movement History
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleEdit}>
          <Pencil className="mr-2 h-4 w-4" />
          Edit
        </DropdownMenuItem>
        <DropdownMenuItem onClick={handleDelete} className="text-destructive">
          <Trash className="mr-2 h-4 w-4" />
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
