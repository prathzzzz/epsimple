import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useAssetExpenditureAndActivityWork } from '../context/asset-expenditure-and-activity-work-provider';

export function AssetExpenditureAndActivityWorkCreateButton() {
  const { openDrawer } = useAssetExpenditureAndActivityWork();

  return (
    <Button onClick={openDrawer} size="sm" className="h-8">
      <Plus className="mr-2 h-4 w-4" />
      Create Expenditure
    </Button>
  );
}
