import { useState } from 'react'
import { MapIcon } from 'lucide-react'
import { ConfigDrawer } from '@/components/config-drawer'
import { Header } from '@/components/layout/header'
import { Main } from '@/components/layout/main'
import { ProfileDropdown } from '@/components/profile-dropdown'
import { Search } from '@/components/search'
import { ThemeSwitch } from '@/components/theme-switch'
import { StatesProvider } from './components/states-provider'
import { StatesTable } from './components/states-table'
import { StatesDialogs } from './components/states-dialogs'
import { StatesPrimaryButtons } from './components/states-primary-buttons'

export default function StatesPage() {
  const [page, setPage] = useState(1)
  const [pageSize, setPageSize] = useState(10)

  return (
    <StatesProvider>
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
              <MapIcon className='h-8 w-8' />
              <h2 className='text-2xl font-bold tracking-tight'>States</h2>
            </div>
            <p className='text-muted-foreground mt-1'>
              Manage state master data
            </p>
          </div>
          <StatesPrimaryButtons />
        </div>
        <div className='-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-y-0 lg:space-x-12'>
          <StatesTable
            page={page}
            pageSize={pageSize}
            onPageChange={setPage}
            onPageSizeChange={setPageSize}
          />
        </div>
      </Main>

      <StatesDialogs />
    </StatesProvider>
  )
}
