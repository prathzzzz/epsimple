import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { useWarehouse } from '../context/warehouse-provider';

export function CreateWarehouseButton() {
  const { openDrawer } = useWarehouse();

  return (
    <Button onClick={openDrawer} size="sm">
      <Plus className="mr-2 h-4 w-4" />
      Create Warehouse
    </Button>
  );
}
