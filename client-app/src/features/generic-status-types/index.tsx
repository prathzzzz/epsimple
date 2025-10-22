import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { GenericStatusTypeProvider } from "./context/generic-status-type-provider";
import { GenericStatusTypeTable } from "./components/generic-status-type-table";
import { genericStatusTypeColumns } from "./components/generic-status-type-columns";
import { GenericStatusTypeDialogs } from "./components/generic-status-type-dialogs";
import { GenericStatusTypePrimaryButtons } from "./components/generic-status-type-primary-buttons";

export default function GenericStatusTypes() {
  return (
    <GenericStatusTypeProvider>
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
              Status Types
            </h2>
            <p className="text-muted-foreground">
              Manage status types and classifications
            </p>
          </div>
          <GenericStatusTypePrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <GenericStatusTypeTable columns={genericStatusTypeColumns} />
        </div>
      </Main>
      <GenericStatusTypeDialogs />
    </GenericStatusTypeProvider>
  );
}
