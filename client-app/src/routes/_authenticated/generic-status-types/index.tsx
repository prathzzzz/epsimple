import { createFileRoute } from "@tanstack/react-router";
import GenericStatusTypes from "@/features/generic-status-types";

export const Route = createFileRoute("/_authenticated/generic-status-types/")({
  component: GenericStatusTypes,
});
