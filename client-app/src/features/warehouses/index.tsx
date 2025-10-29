import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { WarehouseProvider } from './context/warehouse-provider';
import { WarehouseDrawer } from './components/warehouse-drawer';
import { WarehouseDeleteDialog } from './components/warehouse-delete-dialog';
import { WarehouseTable } from './components/warehouse-table';
import { CreateWarehouseButton } from './components/create-warehouse-button';
import { warehouseColumns } from './components/warehouse-columns';

export default function WarehousesPage() {
  return (
    <WarehouseProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Warehouses</h2>
            <p className="text-muted-foreground">
              Manage warehouses across different locations
            </p>
          </div>
          <CreateWarehouseButton />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <WarehouseTable columns={warehouseColumns} />
        </div>
      </Main>
      <WarehouseDrawer />
      <WarehouseDeleteDialog />
    </WarehouseProvider>
  );
}
