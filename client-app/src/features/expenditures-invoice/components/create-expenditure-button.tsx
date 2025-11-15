import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useExpendituresInvoice } from '../hooks/use-expenditures-invoice';

export const CreateExpenditureButton = () => {
  const { openDrawer } = useExpendituresInvoice();

  return (
    <Button onClick={() => openDrawer()}>
      <Plus className="mr-2 h-4 w-4" />
      Add Expenditure
    </Button>
  );
};
