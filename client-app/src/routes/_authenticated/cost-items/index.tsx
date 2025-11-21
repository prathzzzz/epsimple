import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import { CostItemsPage } from '@/features/cost-items';

export const Route = createFileRoute('/_authenticated/cost-items/')({
  component: () => (
    <AdminGuard>
      <CostItemsPage />
    </AdminGuard>
  ),
});
