import { createFileRoute } from '@tanstack/react-router'
import StatesPage from '@/features/states'

export const Route = createFileRoute('/_authenticated/states/')({
  component: StatesPage,
})
