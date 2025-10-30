import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { PersonDetailsProvider } from "./context/person-details-provider";
import { PersonDetailsTable } from "./components/person-details-table";
import { PersonDetailsDrawer } from "./components/person-details-drawer";
import { PersonDetailsDeleteDialog } from "./components/person-details-delete-dialog";
import { CreatePersonDetailsButton } from "./components/create-person-details-button";

function PersonDetailsContent() {
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
            <h2 className="text-2xl font-bold tracking-tight">Person Details</h2>
            <p className="text-muted-foreground">
              Manage all person details and their information
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <CreatePersonDetailsButton />
          </div>
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <PersonDetailsTable />
        </div>
      </Main>
      <PersonDetailsDrawer />
      <PersonDetailsDeleteDialog />
    </>
  );
}

export default function PersonDetailsPage() {
  return (
    <PersonDetailsProvider>
      <PersonDetailsContent />
    </PersonDetailsProvider>
  );
}
