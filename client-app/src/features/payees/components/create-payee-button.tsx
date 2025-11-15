import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { usePayee } from '../hooks/use-payee';

export function CreatePayeeButton() {
  const { openDrawer } = usePayee();

  return (
    <Button onClick={openDrawer}>
      <Plus className="mr-2 h-4 w-4" />
      Create Payee
    </Button>
  );
}
