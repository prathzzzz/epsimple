import { createFileRoute } from '@tanstack/react-router'
import PersonDetailsPage from '@/features/person-details'

export const Route = createFileRoute('/_authenticated/person-details/')({
  component: PersonDetailsPage,
})
