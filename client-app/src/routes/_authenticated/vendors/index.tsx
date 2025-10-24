import { createFileRoute } from '@tanstack/react-router';
import VendorsPage from '@/features/vendors';

export const Route = createFileRoute('/_authenticated/vendors/')({
  component: VendorsPage,
});
