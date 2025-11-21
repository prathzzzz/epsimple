import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import SiteTypes from '@/features/site-types'

export const Route = createFileRoute('/_authenticated/site-types/')({
  component: () => (
    <AdminGuard>
      <SiteTypes />
    </AdminGuard>
  ),
})
