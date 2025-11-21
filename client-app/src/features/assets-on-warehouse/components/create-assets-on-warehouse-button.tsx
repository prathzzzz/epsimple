import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { PermissionGuard } from '@/components/permission-guard';
import { useAssetsOnWarehouse } from '../context/assets-on-warehouse-provider';

export function CreateAssetsOnWarehouseButton() {
  const { openDrawer } = useAssetsOnWarehouse();

  return (
    <PermissionGuard permission="ASSETS_ON_WAREHOUSE:CREATE">
      <Button onClick={openDrawer}>
        <Plus className="mr-2 h-4 w-4" />
        Place Asset in Warehouse
      </Button>
    </PermissionGuard>
  );
}
