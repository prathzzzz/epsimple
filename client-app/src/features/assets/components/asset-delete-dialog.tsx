import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog'
import { useAssetContext } from '../context/asset-provider'
import { assetsApi } from '../api/assets-api'

export function AssetDeleteDialog() {
  const { isDeleteDialogOpen, setIsDeleteDialogOpen, assetToDelete, setAssetToDelete } =
    useAssetContext()
  const deleteAsset = assetsApi.useDelete()

  const handleDelete = () => {
    if (assetToDelete) {
      deleteAsset.mutate(assetToDelete.id, {
        onSuccess: () => {
          setIsDeleteDialogOpen(false)
          setAssetToDelete(null)
        },
      })
    }
  }

  return (
    <AlertDialog open={isDeleteDialogOpen} onOpenChange={setIsDeleteDialogOpen}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This will permanently delete the asset{' '}
            <span className="font-semibold">{assetToDelete?.assetTagId}</span> (
            {assetToDelete?.assetName}). This action cannot be undone.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel
            onClick={() => {
              setAssetToDelete(null)
            }}
          >
            Cancel
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            disabled={deleteAsset.isPending}
          >
            {deleteAsset.isPending ? 'Deleting...' : 'Delete'}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}
