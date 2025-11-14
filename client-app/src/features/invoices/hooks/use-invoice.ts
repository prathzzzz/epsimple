import { useContext } from "react";
import { InvoiceContext, type InvoiceContextType } from "../context";

export const useInvoice = (): InvoiceContextType => {
  const context = useContext(InvoiceContext);
  if (!context) {
    throw new Error("useInvoice must be used within InvoiceProvider");
  }
  return context;
};
