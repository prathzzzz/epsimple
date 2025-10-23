import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { CityProvider } from "./context/city-provider";
import { CityTable } from "./components/city-table";
import { cityColumns } from "./components/city-columns";
import { CreateCityButton } from "./components/create-city-button";
import { CityDrawer } from "./components/city-drawer";
import { CityDeleteDialog } from "./components/city-delete-dialog";

function CitiesContent() {
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
            <h2 className="text-2xl font-bold tracking-tight">Cities</h2>
            <p className="text-muted-foreground">
              Manage cities and their state associations
            </p>
          </div>
          <div className="flex items-center space-x-2">
            <CreateCityButton />
          </div>
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1">
          <CityTable columns={cityColumns} />
        </div>
      </Main>
      <CityDrawer />
      <CityDeleteDialog />
    </>
  );
}

export default function Cities() {
  return (
    <CityProvider>
      <CitiesContent />
    </CityProvider>
  );
}
