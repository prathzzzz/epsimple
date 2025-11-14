import { useContext } from "react";
import { VoucherContext, type VoucherContextType } from "../context";

export const useVoucher = (): VoucherContextType => {
  const context = useContext(VoucherContext);
  if (!context) {
    throw new Error("useVoucher must be used within VoucherProvider");
  }
  return context;
};
