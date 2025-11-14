import { createContext } from "react";
import type { Voucher } from "../api/schema";

export interface VoucherContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  editingVoucher: Voucher | null;
  setEditingVoucher: (voucher: Voucher | null) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  voucherToDelete: Voucher | null;
  setVoucherToDelete: (voucher: Voucher | null) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (open: boolean) => void;
}

export const VoucherContext = createContext<VoucherContextType | undefined>(
  undefined
);
