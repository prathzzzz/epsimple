import { RolesSheet } from './roles-sheet'
import { RolesDeleteDialog } from './roles-delete-dialog'
import { useRoles } from './roles-provider'

export function RolesDialogs() {
  const { open, setOpen, currentRow, setCurrentRow } = useRoles()
  
  return (
    <>
      <RolesSheet
        open={open === 'add' || open === 'edit'}
        onOpenChange={(isOpen) => {
          if (!isOpen) {
            setOpen(null)
            setTimeout(() => {
              setCurrentRow(null)
            }, 300)
          }
        }}
        currentRow={open === 'edit' ? currentRow : null}
      />
      
      <RolesDeleteDialog
        role={currentRow}
        open={open === 'delete'}
        onOpenChange={(isOpen) => {
          if (!isOpen) {
            setOpen(null)
            setTimeout(() => {
              setCurrentRow(null)
            }, 300)
          }
        }}
      />
    </>
  )
}
