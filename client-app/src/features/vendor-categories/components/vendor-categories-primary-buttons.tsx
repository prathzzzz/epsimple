import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useVendorCategories } from '../context/vendor-categories-provider';

export function VendorCategoriesPrimaryButtons() {
  const { setSelectedVendorCategory, setIsDrawerOpen } = useVendorCategories();

  const handleCreate = () => {
    setSelectedVendorCategory(null);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Vendor Category
      </Button>
    </div>
  );
}
