import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { InvoiceProvider } from "./context/invoice-provider";
import { InvoiceTable } from "./components/invoice-table";
import { invoiceColumns } from "./components/invoice-columns";
import { InvoiceDialogs } from "./components/invoice-dialogs";
import { InvoicePrimaryButtons } from "./components/invoice-primary-buttons";

export default function Invoices() {
  return (
    <InvoiceProvider>
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
            <h2 className="text-2xl font-bold tracking-tight">Invoices</h2>
            <p className="text-muted-foreground">
              Manage invoice records and payments
            </p>
          </div>
          <InvoicePrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <InvoiceTable columns={invoiceColumns} />
        </div>
      </Main>
      <InvoiceDialogs />
    </InvoiceProvider>
  );
}
