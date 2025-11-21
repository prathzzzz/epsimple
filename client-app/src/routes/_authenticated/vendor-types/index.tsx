import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import VendorTypes from '@/features/vendor-types';

export const Route = createFileRoute('/_authenticated/vendor-types/')({
  component: () => (
    <AdminGuard>
      <VendorTypes />
    </AdminGuard>
  ),
});
