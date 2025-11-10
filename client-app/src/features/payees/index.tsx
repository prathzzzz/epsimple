import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { PayeeProvider, usePayee } from './context/payee-provider';
import { PayeeDrawer } from './components/payee-drawer';
import { PayeeDeleteDialog } from './components/payee-delete-dialog';
import { PayeeTable } from './components/payee-table';
import { PayeePrimaryButtons } from './components/payee-primary-buttons';
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog';
import { useQueryClient } from '@tanstack/react-query';

function PayeeContent() {
  const { isBulkUploadDialogOpen, setIsBulkUploadDialogOpen } = usePayee();
  const queryClient = useQueryClient();

  return (
    <>
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
            <h2 className="text-2xl font-bold tracking-tight">Payees</h2>
            <p className="text-muted-foreground">
              Manage payees linked to vendors, landlords, and payment details
            </p>
          </div>
          <PayeePrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <PayeeTable />
        </div>
      </Main>
      <PayeeDrawer />
      <PayeeDeleteDialog />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          uploadEndpoint: '/api/payees/bulk-upload',
          errorReportEndpoint: '/api/payees/bulk-upload/errors',
          entityName: 'Payee',
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['payees'] });
          },
        }}
      />
    </>
  );
}

export default function PayeesPage() {
  return (
    <PayeeProvider>
      <PayeeContent />
    </PayeeProvider>
  );
}
