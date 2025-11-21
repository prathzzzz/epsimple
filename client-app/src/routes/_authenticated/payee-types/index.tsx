import { createFileRoute } from "@tanstack/react-router";
import { AdminGuard } from '@/components/admin-guard';
import PayeeTypes from "@/features/payee-types";

export const Route = createFileRoute("/_authenticated/payee-types/")({
  component: () => (
    <AdminGuard>
      <PayeeTypes />
    </AdminGuard>
  ),
});
