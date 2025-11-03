import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import { ArrowRight, History, Info } from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';

interface AssetPlacementConfirmDialogProps {
  readonly open: boolean;
  readonly onOpenChange: (open: boolean) => void;
  readonly onConfirm: () => void;
  readonly assetTagId: string;
  readonly currentLocationType: 'site' | 'warehouse' | 'datacenter';
  readonly currentLocationName: string;
  readonly currentLocationCode: string;
  readonly newLocationType: 'site' | 'warehouse' | 'datacenter';
}

const locationTypeLabels = {
  site: 'Site',
  warehouse: 'Warehouse',
  datacenter: 'Datacenter',
};

export function AssetPlacementConfirmDialog({
  open,
  onOpenChange,
  onConfirm,
  assetTagId,
  currentLocationType,
  currentLocationName,
  currentLocationCode,
  newLocationType,
}: AssetPlacementConfirmDialogProps) {
  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent className="max-w-md">
        <AlertDialogHeader>
          <AlertDialogTitle className="flex items-center gap-2">
            <History className="h-5 w-5" />
            Move Asset
          </AlertDialogTitle>
          <AlertDialogDescription asChild>
            <div className="space-y-4">
              <div className="flex items-center gap-2 text-sm">
                <Info className="h-4 w-4 flex-shrink-0" />
                <p>
                  Asset <span className="font-semibold font-mono">{assetTagId}</span> is currently placed
                </p>
              </div>

              {/* Current Location */}
              <div className="bg-muted/50 p-4 rounded-lg border">
                <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide mb-2">
                  Current Location
                </p>
                <div className="space-y-1">
                  <Badge variant="outline" className="mb-1">
                    {locationTypeLabels[currentLocationType]}
                  </Badge>
                  <p className="font-medium text-foreground">{currentLocationName}</p>
                  <p className="text-sm text-muted-foreground font-mono">{currentLocationCode}</p>
                </div>
              </div>

              {/* Arrow */}
              <div className="flex justify-center">
                <ArrowRight className="h-5 w-5 text-muted-foreground" />
              </div>

              {/* New Location */}
              <div className="bg-primary/5 p-4 rounded-lg border border-primary/20">
                <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide mb-2">
                  New Location
                </p>
                <Badge variant="default">
                  {locationTypeLabels[newLocationType]}
                </Badge>
              </div>

              <Separator />

              {/* Info box */}
              <div className="bg-blue-50 dark:bg-blue-950/30 border border-blue-200 dark:border-blue-900 p-3 rounded-md space-y-2">
                <div className="flex items-start gap-2">
                  <History className="h-4 w-4 text-blue-600 dark:text-blue-400 mt-0.5 flex-shrink-0" />
                  <div className="text-sm space-y-1">
                    <p className="font-medium text-blue-900 dark:text-blue-100">
                      Movement will be tracked
                    </p>
                    <p className="text-blue-700 dark:text-blue-300 text-xs">
                      The current placement will be marked as vacated and the movement will be recorded in the asset's history.
                      You can view the full movement history from the asset details.
                    </p>
                  </div>
                </div>
              </div>

              <p className="text-sm font-medium text-foreground">
                Do you want to proceed with moving this asset?
              </p>
            </div>
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction onClick={onConfirm}>
            <History className="mr-2 h-4 w-4" />
            Move Asset
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
