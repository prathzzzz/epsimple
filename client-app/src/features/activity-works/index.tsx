import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { ActivityWorkProvider } from './context/activity-work-provider';
import { ActivityWorkDrawer } from './components/activity-work-drawer';
import { ActivityWorkDeleteDialog } from './components/activity-work-delete-dialog';
import { ActivityWorkTable } from './components/activity-work-table';
import { CreateActivityWorkButton } from './components/create-activity-work-button';

export default function ActivityWorksPage() {
  return (
    <ActivityWorkProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Activity Work Orders</h2>
            <p className="text-muted-foreground">
              Manage activity work orders and assignments
            </p>
          </div>
          <CreateActivityWorkButton />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <ActivityWorkTable />
        </div>
      </Main>
      <ActivityWorkDrawer />
      <ActivityWorkDeleteDialog />
    </ActivityWorkProvider>
  );
}
