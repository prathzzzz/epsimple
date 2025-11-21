import { createFileRoute } from '@tanstack/react-router'
import SitesPage from '@/features/sites'
import { requirePermission } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/sites/')({
  beforeLoad: () => {
    requirePermission({ permission: 'SITE:READ' })
  },
  component: SitesPage,
})
