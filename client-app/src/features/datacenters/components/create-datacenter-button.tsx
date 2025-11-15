import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { useDatacenter } from '../hooks/use-datacenter';

export function CreateDatacenterButton() {
  const { openDrawer } = useDatacenter();

  return (
    <Button onClick={openDrawer} size="sm">
      <Plus className="mr-2 h-4 w-4" />
      Create Datacenter
    </Button>
  );
}
