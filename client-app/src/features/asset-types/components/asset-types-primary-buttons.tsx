import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useAssetTypes } from '../context/asset-types-provider';

export function AssetTypesPrimaryButtons() {
  const { setSelectedAssetType, setIsDrawerOpen } = useAssetTypes();

  const handleCreate = () => {
    setSelectedAssetType(null);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Asset Type
      </Button>
    </div>
  );
}
