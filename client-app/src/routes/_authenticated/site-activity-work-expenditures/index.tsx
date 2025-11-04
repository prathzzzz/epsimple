import { createFileRoute } from '@tanstack/react-router';
import SiteActivityWorkExpendituresPage from '@/features/site-activity-work-expenditures';

export const Route = createFileRoute('/_authenticated/site-activity-work-expenditures/')({
  component: SiteActivityWorkExpendituresPage,
  validateSearch: (search: Record<string, unknown>) => {
    return {
      siteId: search.siteId as number | undefined,
    };
  },
});
