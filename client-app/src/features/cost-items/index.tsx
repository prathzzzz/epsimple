import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { CostItemProvider } from './cost-item-provider';
import { CostItemDrawer } from './cost-item-drawer';
import { CostItemsTable } from './cost-items-table';
import { CostItemsPrimaryButtons } from './cost-items-primary-buttons';
import { CostItemsDialogs } from './cost-items-dialogs';
import { costItemColumns } from './cost-item-columns';

export const CostItemsPage = () => {
  return (
    <CostItemProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Cost Items</h2>
            <p className="text-muted-foreground">
              Manage cost items for your organization
            </p>
          </div>
          <CostItemsPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <CostItemsTable columns={costItemColumns} />
        </div>
      </Main>
      <CostItemDrawer />
      <CostItemsDialogs />
    </CostItemProvider>
  );
};
