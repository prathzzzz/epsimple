import { createFileRoute } from '@tanstack/react-router'
import { AdminGuard } from '@/components/admin-guard'
import AssetCategoriesPage from '@/features/asset-categories'

export const Route = createFileRoute('/_authenticated/asset-categories/')({
  component: () => (
    <AdminGuard>
      <AssetCategoriesPage />
    </AdminGuard>
  ),
})
