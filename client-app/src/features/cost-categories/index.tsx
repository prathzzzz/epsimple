import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { CostCategoriesProvider } from "./context/cost-categories-provider";
import { CostCategoriesTable } from "./components/cost-categories-table";
import { costCategoriesColumns } from "./components/cost-categories-columns";
import { CostCategoriesDialogs } from "./components/cost-categories-dialogs";
import { CostCategoriesPrimaryButtons } from "./components/cost-categories-primary-buttons";

export default function CostCategories() {
  return (
    <CostCategoriesProvider>
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
              Cost Categories
            </h2>
            <p className="text-muted-foreground">
              Manage cost category classifications
            </p>
          </div>
          <CostCategoriesPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <CostCategoriesTable columns={costCategoriesColumns} />
        </div>
      </Main>
      <CostCategoriesDialogs />
    </CostCategoriesProvider>
  );
}
