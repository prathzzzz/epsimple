import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import CitiesPage from '@/features/cities'

export const Route = createFileRoute('/_authenticated/cities/')({
  component: () => (
    <AdminGuard>
      <CitiesPage />
    </AdminGuard>
  ),
})
