import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import MovementTypes from '@/features/movement-types';

export const Route = createFileRoute('/_authenticated/movement-types/')({
  component: () => (
    <AdminGuard>
      <MovementTypes />
    </AdminGuard>
  ),
});
