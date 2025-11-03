import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useExpendituresInvoiceContext } from '../context/expenditures-invoice-provider';

export const CreateExpenditureButton = () => {
  const { openDrawer } = useExpendituresInvoiceContext();

  return (
    <Button onClick={() => openDrawer()}>
      <Plus className="mr-2 h-4 w-4" />
      Add Expenditure
    </Button>
  );
};
