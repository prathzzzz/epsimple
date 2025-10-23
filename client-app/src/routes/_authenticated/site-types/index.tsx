import { createFileRoute } from '@tanstack/react-router'
import SiteTypes from '@/features/site-types'

export const Route = createFileRoute('/_authenticated/site-types/')({
  component: SiteTypes,
})
