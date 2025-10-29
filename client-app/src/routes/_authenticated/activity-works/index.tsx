import { createFileRoute } from '@tanstack/react-router';
import ActivityWorksPage from '@/features/activity-works';

export const Route = createFileRoute('/_authenticated/activity-works/')({
  component: ActivityWorksPage,
});
