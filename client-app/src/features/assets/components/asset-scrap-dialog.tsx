import { AlertTriangle, Loader2 } from 'lucide-react'
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

export function AssetScrapDialog() {
  const { isScrapDialogOpen, setIsScrapDialogOpen, assetToScrap, setAssetToScrap } = useAssetContext()
  const scrapMutation = assetsApi.useScrap()
  const unscrapMutation = assetsApi.useUnscrap()

  const isScraped = assetToScrap?.isScraped
  const isLoading = scrapMutation.isPending || unscrapMutation.isPending

  const handleConfirm = async () => {
    if (!assetToScrap) return

    if (isScraped) {
      await unscrapMutation.mutateAsync(assetToScrap.id)
    } else {
      await scrapMutation.mutateAsync(assetToScrap.id)
    }
    
    setIsScrapDialogOpen(false)
    setAssetToScrap(null)
  }

  const handleCancel = () => {
    setIsScrapDialogOpen(false)
    setAssetToScrap(null)
  }

  return (
    <AlertDialog open={isScrapDialogOpen} onOpenChange={setIsScrapDialogOpen}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle className="flex items-center gap-2">
            <AlertTriangle className={`h-5 w-5 ${isScraped ? 'text-green-600' : 'text-destructive'}`} />
            {isScraped ? 'Unscrap Asset?' : 'Scrap Asset?'}
          </AlertDialogTitle>
          <AlertDialogDescription>
            {isScraped ? (
              <>
                Are you sure you want to unscrap{' '}
                <span className="font-semibold text-foreground">{assetToScrap?.assetTagId}</span>?
                <br />
                <span className="text-sm">This will remove the scraped date from the asset placement.</span>
              </>
            ) : (
              <>
                Are you sure you want to scrap{' '}
                <span className="font-semibold text-foreground">{assetToScrap?.assetTagId}</span>?
                <br />
                <span className="text-sm">
                  This will mark the asset as scraped with today's date. The asset must be placed at a location.
                </span>
              </>
            )}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel onClick={handleCancel} disabled={isLoading}>
            Cancel
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={handleConfirm}
            disabled={isLoading}
            className={isScraped ? 'bg-green-600 hover:bg-green-700' : 'bg-destructive hover:bg-destructive/90'}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                {isScraped ? 'Unscraping...' : 'Scrapping...'}
              </>
            ) : (
              isScraped ? 'Unscrap' : 'Scrap'
            )}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}
