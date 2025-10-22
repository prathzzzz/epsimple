import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { ActivitiesListProvider } from "./context/activities-list-provider";
import { ActivitiesListTable } from "./components/activities-list-table";
import { activitiesListColumns } from "./components/activities-list-columns";
import { ActivitiesListDialogs } from "./components/activities-list-dialogs";
import { ActivitiesListPrimaryButtons } from "./components/activities-list-primary-buttons";

export default function ActivitiesList() {
  return (
    <ActivitiesListProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Activities</h2>
            <p className="text-muted-foreground">
              Manage detailed activity records and classifications
            </p>
          </div>
          <ActivitiesListPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <ActivitiesListTable columns={activitiesListColumns} />
        </div>
      </Main>
      <ActivitiesListDialogs />
    </ActivitiesListProvider>
  );
}
