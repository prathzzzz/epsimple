import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { SiteProvider } from "./context/site-provider";
import { SiteTable } from "./components/site-table";
import { siteColumns } from "./components/site-columns";
import { SiteDrawer } from "./components/site-drawer";
import { SiteDeleteDialog } from "./components/site-delete-dialog";
import { CreateSiteButton } from "./components/create-site-button";

function SitesContent() {
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
          <div className="flex items-center space-x-2">
            <CreateSiteButton />
          </div>
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <SiteTable columns={siteColumns} />
        </div>
      </Main>
      <SiteDrawer />
      <SiteDeleteDialog />
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
