import { createFileRoute } from '@tanstack/react-router';
import VendorCategories from '@/features/vendor-categories';

export const Route = createFileRoute('/_authenticated/vendor-categories/')({
  component: VendorCategories,
});
