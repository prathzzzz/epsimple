import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { VendorTypesProvider } from './context/vendor-types-provider';
import { VendorTypesTable } from './components/vendor-types-table';
import { vendorTypesColumns } from './components/vendor-types-columns';
import { VendorTypesDialogs } from './components/vendor-types-dialogs';
import { VendorTypesPrimaryButtons } from './components/vendor-types-primary-buttons';
import { ConfigDrawer } from '@/components/config-drawer';

export default function VendorTypes() {
  return (
    <VendorTypesProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Vendor Types</h2>
            <p className="text-muted-foreground">Manage vendor types and their categories</p>
          </div>
          <VendorTypesPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <VendorTypesTable columns={vendorTypesColumns} />
        </div>
      </Main>
      <VendorTypesDialogs />
    </VendorTypesProvider>
  );
}
