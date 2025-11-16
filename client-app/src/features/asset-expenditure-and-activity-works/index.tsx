import { useSearch } from '@tanstack/react-router';
import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { AssetExpenditureAndActivityWorkProvider } from './context/asset-expenditure-and-activity-work-provider';
import { AssetExpenditureAndActivityWorkDrawer } from './components/asset-expenditure-and-activity-work-drawer';
import { AssetExpenditureAndActivityWorkDialogs } from './components/asset-expenditure-and-activity-work-dialogs';
import { AssetExpenditureAndActivityWorkTable } from './components/asset-expenditure-and-activity-work-table';
import { AssetExpenditureAndActivityWorkPrimaryButtons } from './components/asset-expenditure-and-activity-work-primary-buttons';

export default function AssetExpenditureAndActivityWorksPage() {
  const search = useSearch({ from: '/_authenticated/asset-expenditure-and-activity-works/' });
  const assetId = search.assetId;

  return (
    <AssetExpenditureAndActivityWorkProvider assetId={assetId}>
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
            <h2 className="text-2xl font-bold tracking-tight">
              {assetId ? 'Asset ' : ''}Expenditures & Activity Works
            </h2>
            <p className="text-muted-foreground">
              {assetId 
                ? 'Manage expenditures and activity works for this asset'
                : 'Link assets with expenditure invoices and activity works'}
            </p>
          </div>
          <AssetExpenditureAndActivityWorkPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <AssetExpenditureAndActivityWorkTable />
        </div>
      </Main>
      <AssetExpenditureAndActivityWorkDrawer />
      <AssetExpenditureAndActivityWorkDialogs />
    </AssetExpenditureAndActivityWorkProvider>
  );
}
