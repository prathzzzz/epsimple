import React, { useState } from 'react';
import { ExpendituresInvoiceContext } from './expenditures-invoice-context';
import type { ExpendituresInvoice } from '../api/schema';

export const ExpendituresInvoiceProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingExpenditure, setEditingExpenditure] = useState<ExpendituresInvoice | null>(null);
  const [globalFilter, setGlobalFilter] = useState('');
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

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
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
      }}
    >
      {children}
    </ExpendituresInvoiceContext.Provider>
  );
};
