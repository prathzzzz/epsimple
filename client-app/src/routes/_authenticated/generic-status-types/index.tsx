import { createFileRoute } from "@tanstack/react-router";
import { AdminGuard } from '@/components/admin-guard';
import GenericStatusTypes from "@/features/generic-status-types";

export const Route = createFileRoute("/_authenticated/generic-status-types/")({
  component: () => (
    <AdminGuard>
      <GenericStatusTypes />
    </AdminGuard>
  ),
});
