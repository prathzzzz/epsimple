import { useQuery } from '@tanstack/react-query';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Skeleton } from '@/components/ui/skeleton';
import { useAssetContext } from '../context/asset-provider';
import { AssetFinancialCalculator } from '@/features/asset-financials';
import api from '@/lib/api';

interface ApiResponse<T> {
  data: T;
  message: string;
}

interface AssetFinancialData {
  assetId: number;
  assetTagId: string;
  assetName: string;
  assetCategoryName: string;
  depreciationPercentage: number | null;
  purchaseOrderCost: number | null;
  statusTypeName: string;
  // Site data (if placed)
  siteId?: number;
  siteCode?: string;
  siteName?: string;
  techLiveDate?: string;
  // Placement data
  deployedOn?: string;
  activatedOn?: string;
  decommissionedOn?: string;
  vacatedOn?: string;
}

export function AssetFinancialDialog() {
  const { isFinancialDialogOpen, setIsFinancialDialogOpen, assetForFinancial, setAssetForFinancial } =
    useAssetContext();

  // Fetch full financial data for the asset
  const { data: financialData, isLoading } = useQuery({
    queryKey: ['asset-financial-data', assetForFinancial?.id],
    queryFn: async () => {
      const response = await api.get<ApiResponse<AssetFinancialData>>(
        `/api/assets/${assetForFinancial?.id}/financial-details`
      );
      return response.data.data;
    },
    enabled: !!assetForFinancial?.id && isFinancialDialogOpen,
  });

  const handleClose = () => {
    setIsFinancialDialogOpen(false);
    setAssetForFinancial(null);
  };

  if (!assetForFinancial) return null;

  return (
    <Dialog open={isFinancialDialogOpen} onOpenChange={handleClose}>
      <DialogContent className="max-w-3xl max-h-[90vh] p-0" aria-describedby="financial-details-desc">
        <DialogHeader className="px-6 pt-6 pb-2">
          <DialogTitle className="text-lg font-semibold">
            Asset Financial Details
          </DialogTitle>
          <DialogDescription id="financial-details-desc" className="sr-only">
            View depreciation and written down value calculations for this asset
          </DialogDescription>
        </DialogHeader>
        
        <ScrollArea className="max-h-[calc(90vh-100px)] px-6 pb-6">
          {isLoading ? (
            <div className="space-y-4">
              <Skeleton className="h-32 w-full" />
              <Skeleton className="h-24 w-full" />
              <div className="grid grid-cols-2 gap-4">
                <Skeleton className="h-20" />
                <Skeleton className="h-20" />
              </div>
              <Skeleton className="h-48 w-full" />
            </div>
          ) : financialData ? (
            <AssetFinancialCalculator
              assetId={financialData.assetId}
              assetTagId={financialData.assetTagId}
              assetName={financialData.assetName}
              assetCategoryName={financialData.assetCategoryName}
              revisedCapitalValue={financialData.purchaseOrderCost || 0}
              depreciationPercentage={financialData.depreciationPercentage || 0}
              techLiveDate={financialData.techLiveDate}
              deployedOn={financialData.deployedOn || financialData.activatedOn}
              decommissionedOn={financialData.decommissionedOn || financialData.vacatedOn}
              statusTypeName={financialData.statusTypeName}
            />
          ) : (
            <div className="flex items-center justify-center py-10 text-muted-foreground">
              No financial data available
            </div>
          )}
        </ScrollArea>
      </DialogContent>
    </Dialog>
  );
}
