import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { ManagedProjectProvider } from "./context/managed-project-provider";
import { ManagedProjectTable } from "./components/managed-project-table";
import { managedProjectColumns } from "./components/managed-project-columns";
import { ManagedProjectDrawer } from "./components/managed-project-drawer";
import { ManagedProjectDeleteDialog } from "./components/managed-project-delete-dialog";
import { CreateManagedProjectButton } from "./components/create-managed-project-button";

function ManagedProjectsContent() {
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
            <h2 className="text-2xl font-bold tracking-tight">Managed Projects</h2>
            <p className="text-muted-foreground">
              Manage all bank projects and their information
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <CreateManagedProjectButton />
          </div>
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <ManagedProjectTable columns={managedProjectColumns} />
        </div>
      </Main>
      <ManagedProjectDrawer />
      <ManagedProjectDeleteDialog />
    </>
  );
}

export default function ManagedProjects() {
  return (
    <ManagedProjectProvider>
      <ManagedProjectsContent />
    </ManagedProjectProvider>
  );
}
