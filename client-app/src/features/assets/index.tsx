import { Header } from '@/components/layout/header'
import { Main } from '@/components/layout/main'
import { ProfileDropdown } from '@/components/profile-dropdown'
import { Search } from '@/components/search'
import { ThemeSwitch } from '@/components/theme-switch'
import { ConfigDrawer } from '@/components/config-drawer'
import { AssetProvider } from './context/asset-provider'
import { AssetTable } from './components/asset-table'
import { assetColumns } from './components/asset-columns'
import { CreateAssetButton } from './components/create-asset-button'
import { AssetDrawer } from './components/asset-drawer'
import { AssetDeleteDialog } from './components/asset-delete-dialog'

function AssetsContent() {
  return (
    <>
      <Header fixed>
        <Search />
        <div className="ml-auto flex items-center space-x-4">
          <ThemeSwitch />
          <ConfigDrawer />
          <ProfileDropdown />
        </div>
      </Header>
      <Main fixed>
        <div className="mb-2 flex items-center justify-between space-y-2">
          <div>
            <h2 className="text-2xl font-bold tracking-tight">Assets</h2>
            <p className="text-muted-foreground">
              Manage all assets and their details
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <CreateAssetButton />
          </div>
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <AssetTable columns={assetColumns} />
        </div>
      </Main>
      <AssetDrawer />
      <AssetDeleteDialog />
    </>
  )
}

export default function Assets() {
  return (
    <AssetProvider>
      <AssetsContent />
    </AssetProvider>
  )
}
