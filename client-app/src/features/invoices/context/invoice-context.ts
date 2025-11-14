import { createContext } from "react";
import type { Invoice } from "../api/schema";

export interface InvoiceContextType {
  selectedInvoice: Invoice | null;
  setSelectedInvoice: (invoice: Invoice | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (open: boolean) => void;
}

export const InvoiceContext = createContext<InvoiceContextType | undefined>(undefined);
