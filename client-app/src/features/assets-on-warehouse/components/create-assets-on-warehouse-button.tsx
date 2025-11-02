import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useAssetsOnWarehouse } from '../context/assets-on-warehouse-provider';

export function CreateAssetsOnWarehouseButton() {
  const { openDrawer } = useAssetsOnWarehouse();

  return (
    <Button onClick={openDrawer}>
      <Plus className="mr-2 h-4 w-4" />
      Place Asset in Warehouse
    </Button>
  );
}
