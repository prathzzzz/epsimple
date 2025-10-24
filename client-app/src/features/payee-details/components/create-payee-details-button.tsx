import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { usePayeeDetails } from '../context/payee-details-provider';

export function CreatePayeeDetailsButton() {
  const { handleCreate } = usePayeeDetails();

  return (
    <Button onClick={handleCreate}>
      <Plus className="mr-2 h-4 w-4" />
      Create Payee Details
    </Button>
  );
}
