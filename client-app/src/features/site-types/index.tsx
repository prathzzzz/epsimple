import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import {
  SiteTypeProvider,
  useSiteTypeContext,
} from "./context/site-type-provider";
import { SiteTypeTable } from "./components/site-type-table";
import { siteTypeColumns } from "./components/site-type-columns";
import { SiteTypeDialogs } from "./components/site-type-dialogs";
import { SiteTypeMutateDrawer } from "./components/site-type-mutate-drawer";
import { SiteTypePrimaryButtons } from "./components/site-type-primary-buttons";
import { GenericBulkUploadDialog } from "@/components/bulk-upload/GenericBulkUploadDialog";
import { useQueryClient } from "@tanstack/react-query";

function SiteTypesContent() {
  const {
    showMutateDrawer,
    setShowMutateDrawer,
    editingSiteType,
    setEditingSiteType,
    isBulkUploadDialogOpen,
    setIsBulkUploadDialogOpen,
  } = useSiteTypeContext();
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
            <h2 className="text-2xl font-bold tracking-tight">Site Types</h2>
            <p className="text-muted-foreground">
              Manage site type classifications
            </p>
          </div>
          <SiteTypePrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <SiteTypeTable columns={siteTypeColumns} />
        </div>
      </Main>
      <SiteTypeMutateDrawer
        open={showMutateDrawer}
        onOpenChange={(open) => {
          setShowMutateDrawer(open);
          if (!open) {
            setEditingSiteType(null);
          }
        }}
        currentRow={editingSiteType}
      />
      <SiteTypeDialogs />
      <GenericBulkUploadDialog
        open={isBulkUploadDialogOpen}
        onOpenChange={setIsBulkUploadDialogOpen}
        config={{
          uploadEndpoint: "/api/site-types/bulk-upload",
          errorReportEndpoint: "/api/site-types/bulk-upload/errors",
          entityName: "Site Type",
          onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["site-types"] });
          },
        }}
      />
    </>
  );
}

export default function SiteTypes() {
  return (
    <SiteTypeProvider>
      <SiteTypesContent />
    </SiteTypeProvider>
  );
}
