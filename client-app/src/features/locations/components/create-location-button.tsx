import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { useLocation } from '../context/location-provider';

export function CreateLocationButton() {
  const { openDrawer } = useLocation();

  return (
    <Button onClick={openDrawer} size="sm">
      <Plus className="mr-2 h-4 w-4" />
      Create Location
    </Button>
  );
}
