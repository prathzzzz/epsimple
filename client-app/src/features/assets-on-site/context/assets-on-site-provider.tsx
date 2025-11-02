import React, { createContext, useContext, useState, useMemo } from 'react';
import type { AssetsOnSite } from '../api/schema';

interface AssetsOnSiteContextType {
  selectedPlacement: AssetsOnSite | null;
  setSelectedPlacement: (placement: AssetsOnSite | null) => void;
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

interface AssetsOnSiteProviderProps {
  readonly children: React.ReactNode;
}

const AssetsOnSiteContext = createContext<AssetsOnSiteContextType | undefined>(
  undefined
);

export function AssetsOnSiteProvider({ children }: AssetsOnSiteProviderProps) {
  const [selectedPlacement, setSelectedPlacement] = useState<AssetsOnSite | null>(null);
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
    <AssetsOnSiteContext.Provider value={value}>
      {children}
    </AssetsOnSiteContext.Provider>
  );
}

export function useAssetsOnSite() {
  const context = useContext(AssetsOnSiteContext);
  if (!context) {
    throw new Error('useAssetsOnSite must be used within AssetsOnSiteProvider');
  }
  return context;
}
