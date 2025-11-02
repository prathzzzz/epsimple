import React, { createContext, useContext, useState, useMemo } from 'react';
import type { AssetsOnDatacenter } from '../api/schema';

interface AssetsOnDatacenterContextType {
  selectedPlacement: AssetsOnDatacenter | null;
  setSelectedPlacement: (placement: AssetsOnDatacenter | null) => void;
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

interface AssetsOnDatacenterProviderProps {
  readonly children: React.ReactNode;
}

const AssetsOnDatacenterContext = createContext<AssetsOnDatacenterContextType | undefined>(
  undefined
);

export function AssetsOnDatacenterProvider({ children }: AssetsOnDatacenterProviderProps) {
  const [selectedPlacement, setSelectedPlacement] = useState<AssetsOnDatacenter | null>(null);
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
    <AssetsOnDatacenterContext.Provider value={value}>
      {children}
    </AssetsOnDatacenterContext.Provider>
  );
}

export function useAssetsOnDatacenter() {
  const context = useContext(AssetsOnDatacenterContext);
  if (!context) {
    throw new Error('useAssetsOnDatacenter must be used within AssetsOnDatacenterProvider');
  }
  return context;
}
