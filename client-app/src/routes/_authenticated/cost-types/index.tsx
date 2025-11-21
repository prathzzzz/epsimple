import { createFileRoute } from "@tanstack/react-router";
import { AdminGuard } from '@/components/admin-guard';
import CostTypes from "@/features/cost-types";

export const Route = createFileRoute("/_authenticated/cost-types/")({
  component: () => (
    <AdminGuard>
      <CostTypes />
    </AdminGuard>
  ),
});
