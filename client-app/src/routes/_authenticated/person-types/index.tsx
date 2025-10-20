import { createFileRoute } from '@tanstack/react-router'
import PersonTypesPage from '@/features/person-types'

export const Route = createFileRoute('/_authenticated/person-types/')({
  component: PersonTypesPage,
})
