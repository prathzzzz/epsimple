import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useCostItemContext } from './cost-item-provider';

export const CreateCostItemButton = () => {
  const { openDrawer, setEditingCostItem } = useCostItemContext();

  const handleClick = () => {
    setEditingCostItem(null);
    openDrawer();
  };

  return (
    <Button onClick={handleClick}>
      <Plus className="mr-2 h-4 w-4" />
      Create Cost Item
    </Button>
  );
};
