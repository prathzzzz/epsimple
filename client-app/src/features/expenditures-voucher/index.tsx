import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { ConfigDrawer } from '@/components/config-drawer';
import { ExpendituresVoucherProvider } from './context/expenditures-voucher-provider';
import { ExpendituresVoucherTable } from './components/expenditures-voucher-table';
import { ExpendituresVoucherDrawer } from './components/expenditures-voucher-drawer';
import { CreateExpenditureButton } from './components/create-expenditure-button';
import { columns } from './components/columns';

export default function ExpendituresVoucherPage() {
  return (
    <ExpendituresVoucherProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Voucher Expenditures</h2>
            <p className="text-muted-foreground">
              Manage expenditures linked to vouchers
            </p>
          </div>
          <CreateExpenditureButton />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <ExpendituresVoucherTable columns={columns} />
        </div>
      </Main>
      <ExpendituresVoucherDrawer />
    </ExpendituresVoucherProvider>
  );
}
