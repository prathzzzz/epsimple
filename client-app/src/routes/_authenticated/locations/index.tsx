import { createFileRoute } from '@tanstack/react-router';
import LocationsPage from '@/features/locations';

export const Route = createFileRoute('/_authenticated/locations/')({
  component: LocationsPage,
});
