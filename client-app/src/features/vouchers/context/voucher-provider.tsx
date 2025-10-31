import {
  createContext,
  useContext,
  useState,
  type ReactNode,
} from "react";
import type { Voucher } from "../api/schema";

interface VoucherContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  editingVoucher: Voucher | null;
  setEditingVoucher: (voucher: Voucher | null) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  voucherToDelete: Voucher | null;
  setVoucherToDelete: (voucher: Voucher | null) => void;
}

const VoucherContext = createContext<VoucherContextType | undefined>(
  undefined
);

export function VoucherProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingVoucher, setEditingVoucher] = useState<Voucher | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [voucherToDelete, setVoucherToDelete] = useState<Voucher | null>(null);

  return (
    <VoucherContext.Provider
      value={{
        isDrawerOpen,
        setIsDrawerOpen,
        editingVoucher,
        setEditingVoucher,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        voucherToDelete,
        setVoucherToDelete,
      }}
    >
      {children}
    </VoucherContext.Provider>
  );
}

export function useVoucher() {
  const context = useContext(VoucherContext);
  if (context === undefined) {
    throw new Error("useVoucher must be used within a VoucherProvider");
  }
  return context;
}
