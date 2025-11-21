import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import WarehousesPage from '@/features/warehouses';

export const Route = createFileRoute('/_authenticated/warehouses/')({
  component: () => (
    <AdminGuard>
      <WarehousesPage />
    </AdminGuard>
  ),
});
