import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { AssetsOnWarehouseProvider } from './context/assets-on-warehouse-provider';
import { AssetsOnWarehouseDeleteDialog } from './components/assets-on-warehouse-delete-dialog';
import { AssetsOnWarehouseTable } from './components/assets-on-warehouse-table';
import { CreateAssetsOnWarehouseButton } from './components/create-assets-on-warehouse-button';
import { assetsOnWarehouseColumns } from './components/assets-on-warehouse-columns';

export default function AssetsOnWarehousePage() {
  return (
    <AssetsOnWarehouseProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Assets on Warehouse</h2>
            <p className="text-muted-foreground">
              Manage asset placements in warehouses
            </p>
          </div>
          <CreateAssetsOnWarehouseButton />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <AssetsOnWarehouseTable columns={assetsOnWarehouseColumns} />
        </div>
      </Main>
      <AssetsOnWarehouseDeleteDialog />
    </AssetsOnWarehouseProvider>
  );
}
