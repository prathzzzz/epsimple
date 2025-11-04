import { createFileRoute } from '@tanstack/react-router';
import AssetExpenditureAndActivityWorksPage from '@/features/asset-expenditure-and-activity-works';

export const Route = createFileRoute('/_authenticated/asset-expenditure-and-activity-works/')({
  component: AssetExpenditureAndActivityWorksPage,
  validateSearch: (search: Record<string, unknown>) => {
    return {
      assetId: search.assetId as number | undefined,
    };
  },
});
