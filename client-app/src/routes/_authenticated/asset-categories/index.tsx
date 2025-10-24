import { createFileRoute } from '@tanstack/react-router'
import AssetCategoriesPage from '@/features/asset-categories'

export const Route = createFileRoute('/_authenticated/asset-categories/')({
  component: AssetCategoriesPage,
})
