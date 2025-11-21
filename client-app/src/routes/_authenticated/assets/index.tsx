import { createFileRoute } from '@tanstack/react-router'
import Assets from '@/features/assets'
import { requirePermission } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/assets/')({
  beforeLoad: () => {
    requirePermission({ permission: 'ASSET:READ' })
  },
  component: RouteComponent,
})

function RouteComponent() {
  return <Assets />
}
