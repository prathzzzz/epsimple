import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import PersonTypesPage from '@/features/person-types'

export const Route = createFileRoute('/_authenticated/person-types/')({
  component: () => (
    <AdminGuard>
      <PersonTypesPage />
    </AdminGuard>
  ),
})
