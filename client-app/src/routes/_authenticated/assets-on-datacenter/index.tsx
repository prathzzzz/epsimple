import { createFileRoute } from '@tanstack/react-router';
import AssetsOnDatacenterPage from '@/features/assets-on-datacenter';

export const Route = createFileRoute('/_authenticated/assets-on-datacenter/')({
  component: AssetsOnDatacenterPage,
});
