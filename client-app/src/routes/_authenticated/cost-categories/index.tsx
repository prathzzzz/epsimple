import { createFileRoute } from "@tanstack/react-router";
import { AdminGuard } from '@/components/admin-guard';
import CostCategories from "@/features/cost-categories";

export const Route = createFileRoute("/_authenticated/cost-categories/")({
  component: () => (
    <AdminGuard>
      <CostCategories />
    </AdminGuard>
  ),
});
