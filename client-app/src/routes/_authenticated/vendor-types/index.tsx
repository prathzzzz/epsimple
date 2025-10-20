import { createFileRoute } from '@tanstack/react-router';
import VendorTypes from '@/features/vendor-types';

export const Route = createFileRoute('/_authenticated/vendor-types/')({
  component: VendorTypes,
});
