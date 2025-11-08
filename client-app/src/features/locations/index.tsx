import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { LocationProvider } from './context/location-provider';
import { LocationDrawer } from './components/location-drawer';
import { LocationDeleteDialog } from './components/location-delete-dialog';
import { LocationDialogs } from './components/location-dialogs';
import { LocationTable } from './components/location-table';
import { LocationPrimaryButtons } from './components/location-primary-buttons';
import { locationColumns } from './components/location-columns';

export default function LocationsPage() {
  return (
    <LocationProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Locations</h2>
            <p className="text-muted-foreground">
              Manage locations across cities and states
            </p>
          </div>
          <LocationPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <LocationTable columns={locationColumns} />
        </div>
      </Main>
      <LocationDrawer />
      <LocationDeleteDialog />
      <LocationDialogs />
    </LocationProvider>
  );
}
