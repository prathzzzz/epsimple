import { createFileRoute } from '@tanstack/react-router';
import WarehousesPage from '@/features/warehouses';

export const Route = createFileRoute('/_authenticated/warehouses/')({
  component: WarehousesPage,
});
