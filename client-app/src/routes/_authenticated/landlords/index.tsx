import { createFileRoute } from '@tanstack/react-router'
import { Landlords } from '@/features/landlords'
import { requirePermission } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/landlords/')({
  beforeLoad: () => {
    requirePermission({ permission: 'LANDLORD:READ' })
  },
  component: LandlordsPage,
});
