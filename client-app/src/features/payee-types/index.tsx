import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { PayeeTypesProvider } from "./context/payee-types-provider";
import { PayeeTypesTable } from "./components/payee-types-table";
import { payeeTypesColumns } from "./components/payee-types-columns";
import { PayeeTypesDialogs } from "./components/payee-types-dialogs";
import { PayeeTypesPrimaryButtons } from "./components/payee-types-primary-buttons";

export default function PayeeTypes() {
  return (
    <PayeeTypesProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Payee Types</h2>
            <p className="text-muted-foreground">
              Manage payee type classifications
            </p>
          </div>
          <PayeeTypesPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <PayeeTypesTable columns={payeeTypesColumns} />
        </div>
      </Main>
      <PayeeTypesDialogs />
    </PayeeTypesProvider>
  );
}
