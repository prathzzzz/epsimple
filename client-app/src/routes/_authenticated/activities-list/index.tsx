import { createFileRoute } from '@tanstack/react-router'
import { ActivitiesList } from '@/features/activities-list'
import { requirePermission } from '@/lib/route-guards'

export const Route = createFileRoute('/_authenticated/activities-list/')({
  beforeLoad: () => {
    requirePermission({ permission: 'ACTIVITY:READ' })
  },
  component: ActivitiesList,
});
