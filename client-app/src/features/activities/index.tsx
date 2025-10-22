import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { ActivitiesProvider } from "./context/activities-provider";
import { ActivitiesTable } from "./components/activities-table";
import { activitiesColumns } from "./components/activities-columns";
import { ActivitiesDialogs } from "./components/activities-dialogs";
import { ActivitiesPrimaryButtons } from "./components/activities-primary-buttons";

export default function Activities() {
  return (
    <ActivitiesProvider>
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
              Manage activity types and classifications
            </p>
          </div>
          <ActivitiesPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <ActivitiesTable columns={activitiesColumns} />
        </div>
      </Main>
      <ActivitiesDialogs />
    </ActivitiesProvider>
  );
}
