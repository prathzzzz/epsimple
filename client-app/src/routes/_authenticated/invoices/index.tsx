import { createFileRoute } from "@tanstack/react-router";
import Invoices from "@/features/invoices";
import { requirePermission } from "@/lib/route-guards";

export const Route = createFileRoute("/_authenticated/invoices/")({
  beforeLoad: () => {
    requirePermission({ permission: "INVOICE:READ" });
  },
  component: Invoices,
});
