import { AssetCategoryProvider } from "./context/asset-category-provider";
import { assetCategoryColumns } from "./components/asset-category-columns";
import { AssetCategoryTable } from "./components/asset-category-table";
import { AssetCategoryDrawer } from "./components/asset-category-drawer";
import { AssetCategoryDeleteDialog } from "./components/asset-category-delete-dialog";
import { CreateAssetCategoryButton } from "./components/create-asset-category-button";

export default function AssetCategoriesPage() {
  return (
    <AssetCategoryProvider>
      <div className="flex h-full flex-1 flex-col space-y-8 p-8">
        <div className="flex items-center justify-between space-y-2">
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
        <AssetCategoryTable columns={assetCategoryColumns} />
        <AssetCategoryDrawer />
        <AssetCategoryDeleteDialog />
      </div>
    </AssetCategoryProvider>
  );
}
