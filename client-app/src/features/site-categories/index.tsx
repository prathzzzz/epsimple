import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { SiteCategoryProvider } from "./context/site-category-provider";
import { SiteCategoryTable } from "./components/site-category-table";
import { siteCategoryColumns } from "./components/site-category-columns";
import { CreateSiteCategoryButton } from "./components/create-site-category-button";
import { SiteCategoryDrawer } from "./components/site-category-drawer";
import { SiteCategoryDeleteDialog } from "./components/site-category-delete-dialog";

function SiteCategoriesContent() {
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
            <h2 className="text-2xl font-bold tracking-tight">
              Site Categories
            </h2>
            <p className="text-muted-foreground">
              Manage site category classifications
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <CreateSiteCategoryButton />
          </div>
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <SiteCategoryTable columns={siteCategoryColumns} />
        </div>
      </Main>
      <SiteCategoryDrawer />
      <SiteCategoryDeleteDialog />
    </>
  );
}

export default function SiteCategories() {
  return (
    <SiteCategoryProvider>
      <SiteCategoriesContent />
    </SiteCategoryProvider>
  );
}
