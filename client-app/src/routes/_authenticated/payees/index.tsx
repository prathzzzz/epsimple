import { createFileRoute } from '@tanstack/react-router';
import PayeesPage from '@/features/payees';

export const Route = createFileRoute('/_authenticated/payees/')({
  component: PayeesPage,
});
