import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { AssetCategoryProvider } from "./context/asset-category-provider";
import { assetCategoryColumns } from "./components/asset-category-columns";
import { AssetCategoryTable } from "./components/asset-category-table";
import { AssetCategoryDrawer } from "./components/asset-category-drawer";
import { AssetCategoryDeleteDialog } from "./components/asset-category-delete-dialog";
import { CreateAssetCategoryButton } from "./components/create-asset-category-button";

function AssetCategoriesContent() {
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
            <h2 className="text-2xl font-bold tracking-tight">Asset Categories</h2>
            <p className="text-muted-foreground">
              Manage asset categories and their classifications
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <CreateAssetCategoryButton />
          </div>
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <AssetCategoryTable columns={assetCategoryColumns} />
        </div>
      </Main>
      <AssetCategoryDrawer />
      <AssetCategoryDeleteDialog />
    </>
  );
}

export default function AssetCategoriesPage() {
  return (
    <AssetCategoryProvider>
      <AssetCategoriesContent />
    </AssetCategoryProvider>
  );
}
