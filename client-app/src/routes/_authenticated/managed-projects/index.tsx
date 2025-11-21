import { createFileRoute } from '@tanstack/react-router'
import ManagedProjectsPage from '@/features/managed-projects'
import { requirePermission } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/managed-projects/')({
  beforeLoad: () => {
    requirePermission({ permission: 'MANAGED_PROJECT:READ' })
  },
  component: ManagedProjectsPage,
})
