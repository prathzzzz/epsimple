import { createFileRoute } from "@tanstack/react-router";
import PayeeTypes from "@/features/payee-types";

export const Route = createFileRoute("/_authenticated/payee-types/")({
  component: PayeeTypes,
});
