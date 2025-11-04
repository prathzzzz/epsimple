import React, { createContext, useContext, useState } from 'react';
import type { AssetExpenditureAndActivityWork } from '../api/schema';

interface AssetExpenditureAndActivityWorkContextType {
  selectedExpenditure: AssetExpenditureAndActivityWork | null;
  setSelectedExpenditure: (expenditure: AssetExpenditureAndActivityWork | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (show: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
  assetId: number | undefined;
  openDrawer: () => void;
  closeDrawer: () => void;
  openDeleteDialog: () => void;
  closeDeleteDialog: () => void;
}

const AssetExpenditureAndActivityWorkContext = createContext<
  AssetExpenditureAndActivityWorkContextType | undefined
>(undefined);

export function AssetExpenditureAndActivityWorkProvider({
  children,
  assetId,
}: {
  children: React.ReactNode;
  assetId?: number;
}) {
  const [selectedExpenditure, setSelectedExpenditure] =
    useState<AssetExpenditureAndActivityWork | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedExpenditure(null);
  };

  const openDeleteDialog = () => setIsDeleteDialogOpen(true);
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setSelectedExpenditure(null);
  };

  return (
    <AssetExpenditureAndActivityWorkContext.Provider
      value={{
        selectedExpenditure,
        setSelectedExpenditure,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        globalFilter,
        setGlobalFilter,
        assetId,
        openDrawer,
        closeDrawer,
        openDeleteDialog,
        closeDeleteDialog,
      }}
    >
      {children}
    </AssetExpenditureAndActivityWorkContext.Provider>
  );
}

export function useAssetExpenditureAndActivityWork() {
  const context = useContext(AssetExpenditureAndActivityWorkContext);
  if (!context) {
    throw new Error(
      'useAssetExpenditureAndActivityWork must be used within AssetExpenditureAndActivityWorkProvider'
    );
  }
  return context;
}
