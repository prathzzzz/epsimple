import { createFileRoute } from '@tanstack/react-router'

import AssetsOnSitePage from '@/features/assets-on-site'

export const Route = createFileRoute('/_authenticated/assets-on-site/')({
  component: AssetsOnSitePage,
})
