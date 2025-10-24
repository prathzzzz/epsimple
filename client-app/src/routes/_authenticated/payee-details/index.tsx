import { createFileRoute } from '@tanstack/react-router';
import PayeeDetailsPage from '@/features/payee-details';

export const Route = createFileRoute('/_authenticated/payee-details/')({
  component: PayeeDetailsPage,
});
