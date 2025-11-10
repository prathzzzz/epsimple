import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { AssetTypesProvider, useAssetTypes } from './context/asset-types-provider';
import { AssetTypesTable } from './components/asset-types-table';
import { assetTypesColumns } from './components/asset-types-columns';
import { AssetTypesDialogs } from './components/asset-types-dialogs';
import { AssetTypesPrimaryButtons } from './components/asset-types-primary-buttons';
import { ConfigDrawer } from '@/components/config-drawer';
import { GenericBulkUploadDialog } from '@/components/bulk-upload/GenericBulkUploadDialog';
import { useQueryClient } from '@tanstack/react-query';

function AssetTypesContent() {
  const { isBulkUploadDialogOpen, setIsBulkUploadDialogOpen } = useAssetTypes();
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
            <h2 className="text-2xl font-bold tracking-tight">Asset Types</h2>
            <p className="text-muted-foreground">Manage asset type classifications</p>
          </div>
          <AssetTypesPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <AssetTypesTable columns={assetTypesColumns} />
        </div>
      </Main>
      <AssetTypesDialogs />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          uploadEndpoint: '/api/asset-types/bulk-upload',
          errorReportEndpoint: '/api/asset-types/bulk-upload/errors',
          entityName: 'Asset Type',
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['asset-types'] });
          },
        }}
      />
    </>
  );
}

export default function AssetTypes() {
  return (
    <AssetTypesProvider>
      <AssetTypesContent />
    </AssetTypesProvider>
  );
}
