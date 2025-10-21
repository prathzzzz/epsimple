import { createFileRoute } from '@tanstack/react-router';
import AssetTypes from '@/features/asset-types';

export const Route = createFileRoute('/_authenticated/asset-types/')({
  component: AssetTypes,
});
