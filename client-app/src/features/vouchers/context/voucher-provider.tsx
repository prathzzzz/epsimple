import { useState, type ReactNode } from "react";
import { VoucherContext } from "./voucher-context";
import type { Voucher } from "../api/schema";

export function VoucherProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingVoucher, setEditingVoucher] = useState<Voucher | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [voucherToDelete, setVoucherToDelete] = useState<Voucher | null>(null);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

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
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
      }}
    >
      {children}
    </VoucherContext.Provider>
  );
}
