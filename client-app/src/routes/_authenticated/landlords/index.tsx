import { createFileRoute } from '@tanstack/react-router';
import LandlordsPage from '@/features/landlords';

export const Route = createFileRoute('/_authenticated/landlords/')({
  component: LandlordsPage,
});
