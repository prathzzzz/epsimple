import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import StatesPage from '@/features/states'
import { requireAdmin } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/states/')({
  beforeLoad: () => {
    requireAdmin()
  },
  component: () => (
    <AdminGuard>
      <StatesPage />
    </AdminGuard>
  ),
})
