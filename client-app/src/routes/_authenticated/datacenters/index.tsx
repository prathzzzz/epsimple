import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import DatacentersPage from '@/features/datacenters';

export const Route = createFileRoute('/_authenticated/datacenters/')({
  component: () => (
    <AdminGuard>
      <DatacentersPage />
    </AdminGuard>
  ),
});
