import { useSearch } from '@tanstack/react-router';
import { Header } from '@/components/layout/header';
import { Main } from '@/components/layout/main';
import { ProfileDropdown } from '@/components/profile-dropdown';
import { Search } from '@/components/search';
import { ThemeSwitch } from '@/components/theme-switch';
import { ConfigDrawer } from '@/components/config-drawer';
import { SiteActivityWorkExpenditureProvider } from './context/site-activity-work-expenditure-provider';
import { SiteActivityWorkExpenditureDrawer } from './components/site-activity-work-expenditure-drawer';
import { SiteActivityWorkExpenditureDeleteDialog } from './components/site-activity-work-expenditure-delete-dialog';
import { SiteActivityWorkExpenditureTable } from './components/site-activity-work-expenditure-table';
import { SiteActivityWorkExpenditureCreateButton } from './components/site-activity-work-expenditure-create-button';

export default function SiteActivityWorkExpendituresPage() {
  const search = useSearch({ from: '/_authenticated/site-activity-work-expenditures/' });
  const siteId = search.siteId;

  return (
    <SiteActivityWorkExpenditureProvider siteId={siteId}>
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
              {siteId ? 'Site ' : ''}Activity Work Expenditures
            </h2>
            <p className="text-muted-foreground">
              {siteId 
                ? 'Manage activity work expenditures for this site'
                : 'Link sites, activity works, and expenditure invoices'}
            </p>
          </div>
          <SiteActivityWorkExpenditureCreateButton />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <SiteActivityWorkExpenditureTable />
        </div>
      </Main>
      <SiteActivityWorkExpenditureDrawer />
      <SiteActivityWorkExpenditureDeleteDialog />
    </SiteActivityWorkExpenditureProvider>
  );
}
