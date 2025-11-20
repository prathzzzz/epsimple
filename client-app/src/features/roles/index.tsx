import { ConfigDrawer } from '@/components/config-drawer'
import { Header } from '@/components/layout/header'
import { Main } from '@/components/layout/main'
import { ProfileDropdown } from '@/components/profile-dropdown'
import { Search } from '@/components/search'
import { ThemeSwitch } from '@/components/theme-switch'
import { useRoles } from '@/hooks/use-roles'
import { RolesTable } from './components/roles-table.tsx'
import { RolesPrimaryButtons } from './components/roles-primary-buttons'
import { RolesProvider } from './components/roles-provider'
import { RolesDialogs } from './components/roles-dialogs'

export function Roles() {
  const { data: roles = [], isLoading } = useRoles()

  return (
    <RolesProvider>
      <Header fixed>
        <Search />
        <div className='ms-auto flex items-center space-x-4'>
          <ThemeSwitch />
          <ConfigDrawer />
          <ProfileDropdown />
        </div>
      </Header>

      <Main>
        <div className='mb-2 flex flex-wrap items-center justify-between space-y-2'>
          <div>
            <h2 className='text-2xl font-bold tracking-tight'>Roles & Permissions</h2>
            <p className='text-muted-foreground'>
              Manage roles and assign permissions to control user access.
            </p>
          </div>
          <RolesPrimaryButtons />
        </div>
        <div className='-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-y-0'>
          {isLoading ? (
            <div className='flex items-center justify-center h-64'>
              <p className='text-muted-foreground'>Loading roles...</p>
            </div>
          ) : (
            <RolesTable data={roles} />
          )}
        </div>
      </Main>

      <RolesDialogs />
    </RolesProvider>
  )
}
