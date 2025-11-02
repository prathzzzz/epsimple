import { createFileRoute } from '@tanstack/react-router';
import AssetsOnWarehousePage from '@/features/assets-on-warehouse';

export const Route = createFileRoute('/_authenticated/assets-on-warehouse/')({
  component: AssetsOnWarehousePage,
});
