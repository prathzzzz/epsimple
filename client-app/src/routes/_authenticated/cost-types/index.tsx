import { createFileRoute } from "@tanstack/react-router";
import CostTypes from "@/features/cost-types";

export const Route = createFileRoute("/_authenticated/cost-types/")({
  component: CostTypes,
});
