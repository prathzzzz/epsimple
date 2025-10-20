import { createFileRoute } from '@tanstack/react-router'
import { Banks } from '@/features/banks'

export const Route = createFileRoute('/_authenticated/banks/')({
  component: Banks,
})
