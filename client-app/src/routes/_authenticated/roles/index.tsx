import { createFileRoute } from '@tanstack/react-router'
import { Roles } from '@/features/roles/index'
import { requireAdmin } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/roles/')({
  beforeLoad: () => {
    requireAdmin()
  },
  component: Roles,
})
