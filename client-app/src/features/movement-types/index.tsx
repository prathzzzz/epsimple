import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { MovementTypesProvider } from './context/movement-types-provider';
import { MovementTypesTable } from './components/movement-types-table';
import { movementTypesColumns } from './components/movement-types-columns';
import { MovementTypesDialogs } from './components/movement-types-dialogs';
import { MovementTypesPrimaryButtons } from './components/movement-types-primary-buttons';
import { ConfigDrawer } from '@/components/config-drawer';

export default function MovementTypes() {
  return (
    <MovementTypesProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Movement Types</h2>
            <p className="text-muted-foreground">Manage asset movement type classifications</p>
          </div>
          <MovementTypesPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <MovementTypesTable columns={movementTypesColumns} />
        </div>
      </Main>
      <MovementTypesDialogs />
    </MovementTypesProvider>
  );
}
