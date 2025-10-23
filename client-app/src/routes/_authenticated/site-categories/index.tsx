import { createFileRoute } from '@tanstack/react-router'
import SiteCategories from '@/features/site-categories'

export const Route = createFileRoute('/_authenticated/site-categories/')({
  component: SiteCategories,
})
