import { createFileRoute } from '@tanstack/react-router'
import { Roles } from '@/features/roles/index'

export const Route = createFileRoute('/_authenticated/roles/')({
  component: Roles,
})
