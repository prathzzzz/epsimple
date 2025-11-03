import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useExpendituresVoucherContext } from '../context/expenditures-voucher-provider';

export const CreateExpenditureButton = () => {
  const { openDrawer } = useExpendituresVoucherContext();

  return (
    <Button onClick={() => openDrawer()}>
      <Plus className="mr-2 h-4 w-4" />
      Add Expenditure
    </Button>
  );
};
