import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { ConfigDrawer } from '@/components/config-drawer';
import { ExpendituresInvoiceProvider } from './context/expenditures-invoice-provider';
import { ExpendituresInvoiceTable } from './components/expenditures-invoice-table';
import { ExpendituresInvoiceDrawer } from './components/expenditures-invoice-drawer';
import { ExpendituresInvoicePrimaryButtons } from './components/expenditures-invoice-primary-buttons';
import { ExpendituresInvoiceDialogs } from './components/expenditures-invoice-dialogs';
import { columns } from './components/columns';

export default function ExpendituresInvoicePage() {
  return (
    <ExpendituresInvoiceProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Invoice Expenditures</h2>
            <p className="text-muted-foreground">
              Manage expenditures linked to invoices
            </p>
          </div>
          <ExpendituresInvoicePrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <ExpendituresInvoiceTable columns={columns} />
        </div>
      </Main>
      <ExpendituresInvoiceDrawer />
      <ExpendituresInvoiceDialogs />
    </ExpendituresInvoiceProvider>
  );
}
