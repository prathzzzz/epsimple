import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import VendorCategories from '@/features/vendor-categories';

export const Route = createFileRoute('/_authenticated/vendor-categories/')({
  component: () => (
    <AdminGuard>
      <VendorCategories />
    </AdminGuard>
  ),
});
