import { createFileRoute } from "@tanstack/react-router";
import PaymentDetails from "@/features/payment-details";

export const Route = createFileRoute("/_authenticated/payment-details/")({
  component: PaymentDetails,
});
