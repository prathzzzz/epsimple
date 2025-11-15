import { createContext } from 'react';
import type { Payee } from '../api/schema';

export interface PayeeContextType {
  isDrawerOpen: boolean;
  openDrawer: () => void;
  closeDrawer: () => void;
  selectedPayee: Payee | null;
  setSelectedPayee: (payee: Payee | null) => void;
  isDeleteDialogOpen: boolean;
  openDeleteDialog: () => void;
  closeDeleteDialog: () => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (open: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

export const PayeeContext = createContext<PayeeContextType | undefined>(undefined);
