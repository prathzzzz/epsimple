import React, { createContext, useContext, useState } from 'react';
import type { CostItem } from '@/features/cost-items/api/cost-items-api';

interface CostItemContextType {
  isDrawerOpen: boolean;
  openDrawer: () => void;
  closeDrawer: () => void;
  editingCostItem: CostItem | null;
  setEditingCostItem: (costItem: CostItem | null) => void;
}

const CostItemContext = createContext<CostItemContextType | undefined>(undefined);

export const useCostItemContext = () => {
  const context = useContext(CostItemContext);
  if (!context) {
    throw new Error('useCostItemContext must be used within CostItemProvider');
  }
  return context;
};

export const CostItemProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingCostItem, setEditingCostItem] = useState<CostItem | null>(null);

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setEditingCostItem(null);
  };

  return (
    <CostItemContext.Provider
      value={{
        isDrawerOpen,
        openDrawer,
        closeDrawer,
        editingCostItem,
        setEditingCostItem,
      }}
    >
      {children}
    </CostItemContext.Provider>
  );
};
