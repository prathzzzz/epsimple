import { Header } from "@/components/layout/header";
import { Main } from "@/components/layout/main";
import { ProfileDropdown } from "@/components/profile-dropdown";
import { Search } from "@/components/search";
import { ThemeSwitch } from "@/components/theme-switch";
import { ConfigDrawer } from "@/components/config-drawer";
import { VoucherProvider, useVoucher } from "./context/voucher-provider";
import { VoucherTable } from "./components/voucher-table";
import { voucherColumns } from "./components/voucher-columns";
import { VoucherDialogs } from "./components/voucher-dialogs";
import { VoucherPrimaryButtons } from "./components/voucher-primary-buttons";
import { VoucherMutateDrawer } from "./components/voucher-mutate-drawer";

function VoucherContent() {
  const { isDrawerOpen, setIsDrawerOpen, editingVoucher } = useVoucher();

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
            <h2 className="text-2xl font-bold tracking-tight">Vouchers</h2>
            <p className="text-muted-foreground">
              Manage voucher records and payments
            </p>
          </div>
          <VoucherPrimaryButtons />
        </div>
        <div className="-mx-4 flex-1 overflow-auto px-4 py-1 lg:flex-row lg:space-x-12 lg:space-y-0">
          <VoucherTable columns={voucherColumns} />
        </div>
      </Main>
      <VoucherMutateDrawer
        open={isDrawerOpen}
        onOpenChange={setIsDrawerOpen}
        currentRow={editingVoucher}
      />
      <VoucherDialogs />
    </>
  );
}

export default function Vouchers() {
  return (
    <VoucherProvider>
      <VoucherContent />
    </VoucherProvider>
  );
}
