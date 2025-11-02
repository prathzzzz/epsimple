import React, { createContext, useContext, useState, useMemo } from 'react';
import type { AssetsOnWarehouse } from '../api/schema';

interface AssetsOnWarehouseContextType {
  selectedPlacement: AssetsOnWarehouse | null;
  setSelectedPlacement: (placement: AssetsOnWarehouse | null) => void;
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

interface AssetsOnWarehouseProviderProps {
  readonly children: React.ReactNode;
}

const AssetsOnWarehouseContext = createContext<AssetsOnWarehouseContextType | undefined>(
  undefined
);

export function AssetsOnWarehouseProvider({ children }: AssetsOnWarehouseProviderProps) {
  const [selectedPlacement, setSelectedPlacement] = useState<AssetsOnWarehouse | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedPlacement(null);
  };

  const openDeleteDialog = () => setIsDeleteDialogOpen(true);
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setSelectedPlacement(null);
  };

  const value = useMemo(
    () => ({
      selectedPlacement,
      setSelectedPlacement,
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
    }),
    [selectedPlacement, isDrawerOpen, isDeleteDialogOpen, globalFilter]
  );

  return (
    <AssetsOnWarehouseContext.Provider value={value}>
      {children}
    </AssetsOnWarehouseContext.Provider>
  );
}

export function useAssetsOnWarehouse() {
  const context = useContext(AssetsOnWarehouseContext);
  if (!context) {
    throw new Error('useAssetsOnWarehouse must be used within AssetsOnWarehouseProvider');
  }
  return context;
}
