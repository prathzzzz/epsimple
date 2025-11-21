import { createFileRoute } from '@tanstack/react-router';
import { AdminGuard } from '@/components/admin-guard';
import LocationsPage from '@/features/locations';

export const Route = createFileRoute('/_authenticated/locations/')({
  component: () => (
    <AdminGuard>
      <LocationsPage />
    </AdminGuard>
  ),
});
