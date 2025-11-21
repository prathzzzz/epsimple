import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import SiteCategories from '@/features/site-categories'

export const Route = createFileRoute('/_authenticated/site-categories/')({
  component: () => (
    <AdminGuard>
      <SiteCategories />
    </AdminGuard>
  ),
})
