import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useVendorTypes } from '../context/vendor-types-provider';

export function VendorTypesPrimaryButtons() {
  const { setSelectedVendorType, setIsDrawerOpen } = useVendorTypes();

  const handleCreate = () => {
    setSelectedVendorType(null);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Vendor Type
      </Button>
    </div>
  );
}
