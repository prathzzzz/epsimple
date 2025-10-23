import { createFileRoute } from '@tanstack/react-router'
import Cities from '@/features/cities'

export const Route = createFileRoute('/_authenticated/cities/')({
  component: Cities,
})
