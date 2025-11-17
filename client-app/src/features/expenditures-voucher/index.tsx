import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { ConfigDrawer } from '@/components/config-drawer';
import { ExpendituresVoucherProvider, useExpendituresVoucherContext } from './context/expenditures-voucher-provider';
import { ExpendituresVoucherTable } from './components/expenditures-voucher-table';
import { ExpendituresVoucherDrawer } from './components/expenditures-voucher-drawer';
import { ExpendituresVoucherPrimaryButtons } from './components/expenditures-voucher-primary-buttons';
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog';
import { useQueryClient } from '@tanstack/react-query';
import { columns } from './components/columns';

function ExpendituresVoucherContent() {
  const { isBulkUploadDialogOpen, setIsBulkUploadDialogOpen } = useExpendituresVoucherContext();
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
            <h2 className="text-2xl font-bold tracking-tight">Voucher Expenditures</h2>
            <p className="text-muted-foreground">
              Manage expenditures linked to vouchers
            </p>
          </div>
          <ExpendituresVoucherPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <ExpendituresVoucherTable columns={columns} />
        </div>
      </Main>
      <ExpendituresVoucherDrawer />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          uploadEndpoint: '/api/expenditures/vouchers/bulk-upload',
          errorReportEndpoint: '/api/expenditures/vouchers/bulk-upload/errors',
          entityName: 'Expenditures Voucher',
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['expenditures-vouchers'] });
          },
        }}
      />
    </>
  );
}

export default function ExpendituresVoucherPage() {
  return (
    <ExpendituresVoucherProvider>
      <ExpendituresVoucherContent />
    </ExpendituresVoucherProvider>
  );
}
