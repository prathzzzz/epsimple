import { createFileRoute } from "@tanstack/react-router";
import Vouchers from "@/features/vouchers";
import { requirePermission } from "@/lib/route-guards";

export const Route = createFileRoute("/_authenticated/vouchers/")({
  beforeLoad: () => {
    requirePermission({ permission: "VOUCHER:READ" });
  },
  component: Vouchers,
});

