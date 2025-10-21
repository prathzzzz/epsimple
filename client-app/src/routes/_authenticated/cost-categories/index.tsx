import { createFileRoute } from "@tanstack/react-router";
import CostCategories from "@/features/cost-categories";

export const Route = createFileRoute("/_authenticated/cost-categories/")({
  component: CostCategories,
});
