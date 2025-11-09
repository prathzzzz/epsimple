import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { VendorProvider } from './vendor-provider';
import { VendorDrawer } from './vendor-drawer';
import { VendorsTable } from './vendors-table';
import { VendorPrimaryButtons } from './components/vendor-primary-buttons';
import { VendorDialogs } from './components/vendor-dialogs';
import { vendorColumns } from './vendor-columns';

export default function VendorsPage() {
  return (
    <VendorProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Vendors</h2>
            <p className="text-muted-foreground">
              Manage vendor information and relationships
            </p>
          </div>
          <VendorPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <VendorsTable columns={vendorColumns} />
        </div>
      </Main>
      <VendorDrawer />
      <VendorDialogs />
    </VendorProvider>
  );
}
