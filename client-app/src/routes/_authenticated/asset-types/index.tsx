import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import AssetTypes from '@/features/asset-types';

export const Route = createFileRoute('/_authenticated/asset-types/')({
  component: () => (
    <AdminGuard>
      <AssetTypes />
    </AdminGuard>
  ),
});
