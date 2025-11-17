import { createContext, useContext, useState, type ReactNode } from 'react';
import type { ExpendituresVoucher } from '../api/schema';

interface ExpendituresVoucherContextType {
  isDrawerOpen: boolean;
  openDrawer: (expenditure?: ExpendituresVoucher) => void;
  closeDrawer: () => void;
  editingExpenditure: ExpendituresVoucher | null;
  setEditingExpenditure: (expenditure: ExpendituresVoucher | null) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (open: boolean) => void;
}

const ExpendituresVoucherContext = createContext<ExpendituresVoucherContextType | undefined>(
  undefined
);

export const useExpendituresVoucherContext = () => {
  const context = useContext(ExpendituresVoucherContext);
  if (!context) {
    throw new Error(
      'useExpendituresVoucherContext must be used within ExpendituresVoucherProvider'
    );
  }
  return context;
};

export const ExpendituresVoucherProvider = ({ children }: { children: ReactNode }) => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingExpenditure, setEditingExpenditure] = useState<ExpendituresVoucher | null>(null);
  const [globalFilter, setGlobalFilter] = useState('');
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

  const openDrawer = (expenditure?: ExpendituresVoucher) => {
    if (expenditure) {
      setEditingExpenditure(expenditure);
    } else {
      setEditingExpenditure(null);
    }
    setIsDrawerOpen(true);
  };

  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setEditingExpenditure(null);
  };

  return (
    <ExpendituresVoucherContext.Provider
      value={{
        isDrawerOpen,
        openDrawer,
        closeDrawer,
        editingExpenditure,
        setEditingExpenditure,
        globalFilter,
        setGlobalFilter,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
      }}
    >
      {children}
    </ExpendituresVoucherContext.Provider>
  );
};
