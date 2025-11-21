import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import StatesPage from '@/features/states'

export const Route = createFileRoute('/_authenticated/states/')({
  component: () => (
    <AdminGuard>
      <StatesPage />
    </AdminGuard>
  ),
})
