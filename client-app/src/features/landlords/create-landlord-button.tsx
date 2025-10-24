import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useLandlordContext } from './landlord-provider';

export const CreateLandlordButton = () => {
  const { openCreateDrawer } = useLandlordContext();

  return (
    <div className="flex items-center justify-between space-y-2">
      <Button onClick={openCreateDrawer}>
        <Plus className="mr-2 h-4 w-4" />
        Add Landlord
      </Button>
    </div>
  );
};
