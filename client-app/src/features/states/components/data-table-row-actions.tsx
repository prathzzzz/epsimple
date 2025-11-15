import { DotsHorizontalIcon } from '@radix-ui/react-icons'
import type { Row } from '@tanstack/react-table'
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import type { State } from '../data/schema'
import { useStates } from '../hooks/use-states'

interface DataTableRowActionsProps {
  row: Row<State>
}

export function DataTableRowActions({ row }: DataTableRowActionsProps) {
  const { setSelectedState, openDrawer, openDeleteDialog, setIsEditMode } = useStates()

  const handleEdit = () => {
    setSelectedState(row.original)
    setIsEditMode(true)
    openDrawer()
  }

  const handleDelete = () => {
    setSelectedState(row.original)
    openDeleteDialog()
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="flex h-8 w-8 p-0 data-[state=open]:bg-muted"
        >
          <DotsHorizontalIcon className="h-4 w-4" />
          <span className="sr-only">Open menu</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-[160px]">
        <DropdownMenuItem onClick={handleEdit}>Edit</DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleDelete} className="text-destructive">
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
