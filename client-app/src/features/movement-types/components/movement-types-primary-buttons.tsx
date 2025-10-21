import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useMovementTypes } from '../context/movement-types-provider';

export function MovementTypesPrimaryButtons() {
  const { setSelectedMovementType, setIsDrawerOpen } = useMovementTypes();

  const handleCreate = () => {
    setSelectedMovementType(null);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Movement Type
      </Button>
    </div>
  );
}
