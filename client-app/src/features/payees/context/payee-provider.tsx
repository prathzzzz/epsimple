import { createContext, useContext, useState, type ReactNode } from 'react';
import type { Payee } from '../api/schema';

interface PayeeContextType {
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

const PayeeContext = createContext<PayeeContextType | undefined>(undefined);

export function PayeeProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [selectedPayee, setSelectedPayee] = useState<Payee | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedPayee(null);
  };

  const openDeleteDialog = () => setIsDeleteDialogOpen(true);
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setSelectedPayee(null);
  };

  return (
    <PayeeContext.Provider
      value={{
        isDrawerOpen,
        openDrawer,
        closeDrawer,
        selectedPayee,
        setSelectedPayee,
        isDeleteDialogOpen,
        openDeleteDialog,
        closeDeleteDialog,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </PayeeContext.Provider>
  );
}

export function usePayee() {
  const context = useContext(PayeeContext);
  if (!context) {
    throw new Error('usePayee must be used within a PayeeProvider');
  }
  return context;
}
