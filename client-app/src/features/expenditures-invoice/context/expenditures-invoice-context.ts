import { createContext } from 'react';
import type { ExpendituresInvoice } from '../api/schema';

export interface ExpendituresInvoiceContextType {
  isDrawerOpen: boolean;
  openDrawer: () => void;
  closeDrawer: () => void;
  editingExpenditure: ExpendituresInvoice | null;
  setEditingExpenditure: (expenditure: ExpendituresInvoice | null) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (open: boolean) => void;
}

export const ExpendituresInvoiceContext = createContext<ExpendituresInvoiceContextType | undefined>(undefined);
