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
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Asset Already Placed</AlertDialogTitle>
          <AlertDialogDescription asChild>
            <div className="space-y-2">
              <p>
                Asset <span className="font-semibold">{assetTagId}</span> is already placed at:
              </p>
              <div className="bg-muted p-3 rounded-md">
                <p className="font-medium">
                  {locationTypeLabels[currentLocationType]}: {currentLocationName}
                </p>
                <p className="text-sm text-muted-foreground">Code: {currentLocationCode}</p>
              </div>
              <p className="pt-2">
                Placing it on this {locationTypeLabels[newLocationType].toLowerCase()} will
                automatically remove it from the current location.
              </p>
              <p className="font-semibold">Are you sure you want to continue?</p>
            </div>
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction onClick={onConfirm}>Yes, Move Asset</AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
