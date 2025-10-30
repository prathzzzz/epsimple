import { createFileRoute } from '@tanstack/react-router'
import AssetTagGenerators from '@/features/asset-tag-generators'

export const Route = createFileRoute('/_authenticated/asset-tag-generators/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <AssetTagGenerators />
}
