import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { DatacenterProvider } from './context/datacenter-provider';
import { DatacenterDrawer } from './components/datacenter-drawer';
import { DatacenterDeleteDialog } from './components/datacenter-delete-dialog';
import { DatacenterTable } from './components/datacenter-table';
import { CreateDatacenterButton } from './components/create-datacenter-button';
import { datacenterColumns } from './components/datacenter-columns';

export default function DatacentersPage() {
  return (
    <DatacenterProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Datacenters</h2>
            <p className="text-muted-foreground">
              Manage datacenters across different locations
            </p>
          </div>
          <CreateDatacenterButton />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <DatacenterTable columns={datacenterColumns} />
        </div>
      </Main>
      <DatacenterDrawer />
      <DatacenterDeleteDialog />
    </DatacenterProvider>
  );
}
