import { createFileRoute } from '@tanstack/react-router';
import DatacentersPage from '@/features/datacenters';

export const Route = createFileRoute('/_authenticated/datacenters/')({
  component: DatacentersPage,
});
