import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { PayeeDetailsProvider } from './context/payee-details-provider';
import { PayeeDetailsDrawer } from './components/payee-details-drawer';
import { PayeeDetailsDeleteDialog } from './components/payee-details-delete-dialog';
import { PayeeDetailsTable } from './components/payee-details-table';
import { CreatePayeeDetailsButton } from './components/create-payee-details-button';
import { payeeDetailsColumns } from './components/payee-details-columns';

export default function PayeeDetailsPage() {
  return (
    <PayeeDetailsProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Payee Details</h2>
            <p className="text-muted-foreground">
              Manage payee banking and identification details
            </p>
          </div>
          <CreatePayeeDetailsButton />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <PayeeDetailsTable columns={payeeDetailsColumns} />
        </div>
      </Main>
      <PayeeDetailsDrawer />
      <PayeeDetailsDeleteDialog />
    </PayeeDetailsProvider>
  );
}
