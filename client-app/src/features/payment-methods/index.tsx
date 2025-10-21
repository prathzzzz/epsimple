import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { PaymentMethodsProvider } from "./context/payment-methods-provider";
import { PaymentMethodsTable } from "./components/payment-methods-table";
import { paymentMethodsColumns } from "./components/payment-methods-columns";
import { PaymentMethodsDialogs } from "./components/payment-methods-dialogs";
import { PaymentMethodsPrimaryButtons } from "./components/payment-methods-primary-buttons";

export default function PaymentMethods() {
  return (
    <PaymentMethodsProvider>
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
              Payment Methods
            </h2>
            <p className="text-muted-foreground">
              Manage payment method types
            </p>
          </div>
          <PaymentMethodsPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <PaymentMethodsTable columns={paymentMethodsColumns} />
        </div>
      </Main>
      <PaymentMethodsDialogs />
    </PaymentMethodsProvider>
  );
}
