import React, { createContext, useContext, useState } from 'react';
import type { Warehouse } from '../api/schema';

interface WarehouseContextType {
  selectedWarehouse: Warehouse | null;
  setSelectedWarehouse: (warehouse: Warehouse | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (show: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
  openDrawer: () => void;
  closeDrawer: () => void;
  openDeleteDialog: () => void;
  closeDeleteDialog: () => void;
}

const WarehouseContext = createContext<WarehouseContextType | undefined>(
  undefined
);

export function WarehouseProvider({ children }: { children: React.ReactNode }) {
  const [selectedWarehouse, setSelectedWarehouse] = useState<Warehouse | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedWarehouse(null);
  };

  const openDeleteDialog = () => setIsDeleteDialogOpen(true);
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setSelectedWarehouse(null);
  };

  return (
    <WarehouseContext.Provider
      value={{
        selectedWarehouse,
        setSelectedWarehouse,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        globalFilter,
        setGlobalFilter,
        openDrawer,
        closeDrawer,
        openDeleteDialog,
        closeDeleteDialog,
      }}
    >
      {children}
    </WarehouseContext.Provider>
  );
}

export function useWarehouse() {
  const context = useContext(WarehouseContext);
  if (!context) {
    throw new Error('useWarehouse must be used within WarehouseProvider');
  }
  return context;
}
