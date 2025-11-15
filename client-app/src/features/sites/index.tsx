import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { SiteProvider } from "./context/site-provider";
import { useSite } from "./hooks/use-site";
import { SiteTable } from "./components/site-table";
import { siteColumns } from "./components/site-columns";
import { SiteDrawer } from "./components/site-drawer";
import { SiteDeleteDialog } from "./components/site-delete-dialog";
import { SitePrimaryButtons } from "./components/site-primary-buttons";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { useQueryClient } from "@tanstack/react-query";

function SitesContent() {
  const { isBulkUploadDialogOpen, setIsBulkUploadDialogOpen } = useSite();
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
            <h2 className="text-2xl font-bold tracking-tight">Sites</h2>
            <p className="text-muted-foreground">
              Manage all sites and their information
            </p>
          </div>
          <SitePrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <SiteTable columns={siteColumns} />
        </div>
      </Main>
      <SiteDrawer />
      <SiteDeleteDialog />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          uploadEndpoint: '/api/sites/bulk-upload',
          errorReportEndpoint: '/api/sites/bulk-upload/errors',
          entityName: 'Site',
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['sites'] });
          },
        }}
      />
    </>
  );
}

export default function Sites() {
  return (
    <SiteProvider>
      <SitesContent />
    </SiteProvider>
  );
}
