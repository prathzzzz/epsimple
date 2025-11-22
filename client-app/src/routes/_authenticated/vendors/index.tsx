import { createFileRoute } from '@tanstack/react-router'
import VendorsPage from '@/features/vendors'
import { requirePermission } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/vendors/')({
  beforeLoad: () => {
    requirePermission({ permission: 'VENDOR:READ' })
  },
  component: VendorsPage,
});
