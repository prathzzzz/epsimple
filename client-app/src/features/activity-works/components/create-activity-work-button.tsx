import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { useActivityWork } from '../context/activity-work-provider';

export function CreateActivityWorkButton() {
  const { openDrawer } = useActivityWork();

  return (
    <Button onClick={openDrawer} size="sm">
      <Plus className="mr-2 h-4 w-4" />
      Create Activity Work
    </Button>
  );
}
