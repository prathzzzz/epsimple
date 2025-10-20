import { useState } from 'react'
import { Users } from 'lucide-react'
import { ConfigDrawer } from '@/components/config-drawer'
import { Header } from '@/components/layout/header'
import { Main } from '@/components/layout/main'
import { ProfileDropdown } from '@/components/profile-dropdown'
import { Search } from '@/components/search'
import { ThemeSwitch } from '@/components/theme-switch'
import { PersonTypesProvider } from './components/person-types-provider'
import { PersonTypesTable } from './components/person-types-table'
import { PersonTypesDialogs } from './components/person-types-dialogs'
import { PersonTypesPrimaryButtons } from './components/person-types-primary-buttons'

export default function PersonTypesPage() {
  const [page, setPage] = useState(1)
  const [pageSize, setPageSize] = useState(10)

  return (
    <PersonTypesProvider>
      <Header fixed>
        <Search />
        <div className='ms-auto flex items-center space-x-4'>
          <ThemeSwitch />
          <ConfigDrawer />
          <ProfileDropdown />
        </div>
      </Header>

      <Main>
        <div className='mb-2 flex flex-wrap items-center justify-between space-y-2 gap-x-4'>
          <div>
            <div className='flex items-center gap-2'>
              <Users className='h-8 w-8' />
              <h2 className='text-2xl font-bold tracking-tight'>Person Types</h2>
            </div>
            <p className='text-muted-foreground mt-1'>
              Manage person types and their information
            </p>
          </div>
          <PersonTypesPrimaryButtons />
        </div>
        <div className='-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-y-0 lg:space-x-12'>
          <PersonTypesTable
            page={page}
            pageSize={pageSize}
            onPageChange={setPage}
            onPageSizeChange={setPageSize}
          />
        </div>
      </Main>

      <PersonTypesDialogs />
    </PersonTypesProvider>
  )
}
