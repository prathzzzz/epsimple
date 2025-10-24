import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useVendorContext } from './vendor-provider';

export const CreateVendorButton = () => {
  const { openCreateDrawer } = useVendorContext();

  return (
    <div className="flex items-center justify-between space-y-2">
      <Button onClick={openCreateDrawer}>
        <Plus className="mr-2 h-4 w-4" />
        Add Vendor
      </Button>
    </div>
  );
};
