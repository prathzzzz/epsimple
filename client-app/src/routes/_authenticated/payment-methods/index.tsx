import { createFileRoute } from "@tanstack/react-router";
import { AdminGuard } from '@/components/admin-guard';
import PaymentMethods from "@/features/payment-methods";

export const Route = createFileRoute("/_authenticated/payment-methods/")({
  component: () => (
    <AdminGuard>
      <PaymentMethods />
    </AdminGuard>
  ),
});
