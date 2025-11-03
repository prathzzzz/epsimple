import React, { createContext, useContext, useState } from 'react';
import type { ExpendituresInvoice } from '../api/schema';

interface ExpendituresInvoiceContextType {
  isDrawerOpen: boolean;
  openDrawer: () => void;
  closeDrawer: () => void;
  editingExpenditure: ExpendituresInvoice | null;
  setEditingExpenditure: (expenditure: ExpendituresInvoice | null) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const ExpendituresInvoiceContext = createContext<ExpendituresInvoiceContextType | undefined>(undefined);

export const useExpendituresInvoiceContext = () => {
  const context = useContext(ExpendituresInvoiceContext);
  if (!context) {
    throw new Error('useExpendituresInvoiceContext must be used within ExpendituresInvoiceProvider');
  }
  return context;
};

export const ExpendituresInvoiceProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingExpenditure, setEditingExpenditure] = useState<ExpendituresInvoice | null>(null);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setEditingExpenditure(null);
  };

  return (
    <ExpendituresInvoiceContext.Provider
      value={{
        isDrawerOpen,
        openDrawer,
        closeDrawer,
        editingExpenditure,
        setEditingExpenditure,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </ExpendituresInvoiceContext.Provider>
  );
};
