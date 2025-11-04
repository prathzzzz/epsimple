import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useSiteActivityWorkExpenditure } from '../context/site-activity-work-expenditure-provider';

export function SiteActivityWorkExpenditureCreateButton() {
  const { openDrawer } = useSiteActivityWorkExpenditure();

  return (
    <Button onClick={openDrawer} size="sm" className="h-8">
      <Plus className="mr-2 h-4 w-4" />
      Create Expenditure
    </Button>
  );
}
