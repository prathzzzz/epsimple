import { createFileRoute } from '@tanstack/react-router'
import SiteCodeGeneratorsPage from '@/features/site-code-generators'

export const Route = createFileRoute('/_authenticated/site-code-generators/')({
  component: SiteCodeGeneratorsPage,
})
