import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useAssetsOnDatacenter } from '../context/assets-on-datacenter-provider';

export function CreateAssetsOnDatacenterButton() {
  const { openDrawer } = useAssetsOnDatacenter();

  return (
    <Button onClick={openDrawer}>
      <Plus className="mr-2 h-4 w-4" />
      Place Asset in Datacenter
    </Button>
  );
}
