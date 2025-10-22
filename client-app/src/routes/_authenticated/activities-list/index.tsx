import { createFileRoute } from "@tanstack/react-router";
import ActivitiesList from "@/features/activities-list";

export const Route = createFileRoute("/_authenticated/activities-list/")({
  component: ActivitiesList,
});
