import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { VendorCategoriesProvider } from './context/vendor-categories-provider';
import { VendorCategoriesTable } from './components/vendor-categories-table';
import { vendorCategoriesColumns } from './components/vendor-categories-columns';
import { VendorCategoriesDialogs } from './components/vendor-categories-dialogs';
import { VendorCategoriesPrimaryButtons } from './components/vendor-categories-primary-buttons';
import { ConfigDrawer } from '@/components/config-drawer';

export default function VendorCategories() {
  return (
    <VendorCategoriesProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Vendor Categories</h2>
            <p className="text-muted-foreground">Manage vendor category classifications</p>
          </div>
          <VendorCategoriesPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <VendorCategoriesTable columns={vendorCategoriesColumns} />
        </div>
      </Main>
      <VendorCategoriesDialogs />
    </VendorCategoriesProvider>
  );
}
