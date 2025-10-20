import { useState } from 'react'
import { Landmark } from 'lucide-react'
import { ConfigDrawer } from '@/components/config-drawer'
import { Header } from '@/components/layout/header'
import { Main } from '@/components/layout/main'
import { ProfileDropdown } from '@/components/profile-dropdown'
import { Search } from '@/components/search'
import { ThemeSwitch } from '@/components/theme-switch'
import { BanksDialogs } from './components/banks-dialogs'
import { BanksPrimaryButtons } from './components/banks-primary-buttons'
import { BanksProvider } from './components/banks-provider'
import { BanksTable } from './components/banks-table'

export function Banks() {
  const [page, setPage] = useState(1)
  const [pageSize, setPageSize] = useState(10)

  return (
    <BanksProvider>
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
              <Landmark className='h-8 w-8' />
              <h2 className='text-2xl font-bold tracking-tight'>Banks</h2>
            </div>
            <p className='text-muted-foreground mt-1'>
              Manage bank master data
            </p>
          </div>
          <BanksPrimaryButtons />
        </div>
        <div className='-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-y-0 lg:space-x-12'>
          <BanksTable
            page={page}
            pageSize={pageSize}
            onPageChange={setPage}
            onPageSizeChange={setPageSize}
          />
        </div>
      </Main>

      <BanksDialogs />
    </BanksProvider>
  )
}
