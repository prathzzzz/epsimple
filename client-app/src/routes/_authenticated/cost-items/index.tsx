import { createFileRoute } from '@tanstack/react-router';
import { CostItemsPage } from '@/features/cost-items';

export const Route = createFileRoute('/_authenticated/cost-items/')({
  component: CostItemsPage,
});
