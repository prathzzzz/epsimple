import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import { Banks } from '@/features/banks'

export const Route = createFileRoute('/_authenticated/banks/')({
  component: () => (
    <AdminGuard>
      <Banks />
    </AdminGuard>
  ),
})
