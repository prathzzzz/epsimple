import { type Row } from '@tanstack/react-table'
import { MoreHorizontal, Pen, Trash2 } from 'lucide-react'
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import type { PersonType } from '../data/schema'
import { usePersonTypes } from './person-types-provider'

interface DataTableRowActionsProps {
  row: Row<PersonType>
}

export function DataTableRowActions({ row }: DataTableRowActionsProps) {
  const personType = row.original
  const {
    setSelectedPersonType,
    setIsDrawerOpen,
    setIsDeleteDialogOpen,
  } = usePersonTypes()

  const handleEdit = () => {
    setSelectedPersonType(personType)
    setIsDrawerOpen(true)
  }

  const handleDelete = () => {
    setSelectedPersonType(personType)
    setIsDeleteDialogOpen(true)
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant='ghost'
          className='flex size-8 p-0 data-[state=open]:bg-muted'
        >
          <MoreHorizontal className='size-4' />
          <span className='sr-only'>Open menu</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align='end' className='w-[160px]'>
        <DropdownMenuItem onClick={handleEdit}>
          <Pen className='mr-2 size-4' />
          Edit
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleDelete} className='text-destructive'>
          <Trash2 className='mr-2 size-4' />
          Delete
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
