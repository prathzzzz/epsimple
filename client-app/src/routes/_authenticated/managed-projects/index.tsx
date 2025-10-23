import { createFileRoute } from '@tanstack/react-router'
import ManagedProjectsPage from '@/features/managed-projects'

export const Route = createFileRoute('/_authenticated/managed-projects/')({
  component: ManagedProjectsPage,
})
