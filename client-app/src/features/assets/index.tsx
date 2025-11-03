import { Header } from '@/components/layout/header'
import { Main } from '@/components/layout/main'
import { ProfileDropdown } from '@/components/profile-dropdown'
import { Search } from '@/components/search'
import { ThemeSwitch } from '@/components/theme-switch'
import { ConfigDrawer } from '@/components/config-drawer'
import { AssetProvider, useAssetContext } from './context/asset-provider'
import { AssetTable } from './components/asset-table'
import { assetColumns } from './components/asset-columns'
import { CreateAssetButton } from './components/create-asset-button'
import { AssetDrawer } from './components/asset-drawer'
import { AssetDeleteDialog } from './components/asset-delete-dialog'
import { AssetMovementDialog } from './components/asset-movement-dialog'
import { AssetPlacementDialog } from './components/asset-placement-dialog'

function AssetsContent() {
  const { 
    isMovementDialogOpen, 
    setIsMovementDialogOpen, 
    assetForMovement,
    isPlacementDialogOpen,
    setIsPlacementDialogOpen,
    assetForPlacement,
  } = useAssetContext();

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
      {assetForMovement && (
        <AssetMovementDialog
          open={isMovementDialogOpen}
          onOpenChange={setIsMovementDialogOpen}
          assetId={assetForMovement.id}
          assetTagId={assetForMovement.assetTagId}
          assetName={assetForMovement.assetName}
        />
      )}
      {assetForPlacement && (
        <AssetPlacementDialog
          open={isPlacementDialogOpen}
          onOpenChange={setIsPlacementDialogOpen}
          assetId={assetForPlacement.id}
          assetTagId={assetForPlacement.assetTagId}
          assetName={assetForPlacement.assetName}
        />
      )}
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
