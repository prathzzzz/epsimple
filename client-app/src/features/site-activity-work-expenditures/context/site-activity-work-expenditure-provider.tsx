import React, { createContext, useContext, useState } from 'react';
import type { SiteActivityWorkExpenditure } from '../api/schema';

interface SiteActivityWorkExpenditureContextType {
  selectedExpenditure: SiteActivityWorkExpenditure | null;
  setSelectedExpenditure: (expenditure: SiteActivityWorkExpenditure | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (show: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (show: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
  siteId: number | undefined;
  openDrawer: () => void;
  closeDrawer: () => void;
  openDeleteDialog: () => void;
  closeDeleteDialog: () => void;
}

const SiteActivityWorkExpenditureContext = createContext<
  SiteActivityWorkExpenditureContextType | undefined
>(undefined);

export function SiteActivityWorkExpenditureProvider({
  children,
  siteId,
}: {
  children: React.ReactNode;
  siteId?: number;
}) {
  const [selectedExpenditure, setSelectedExpenditure] =
    useState<SiteActivityWorkExpenditure | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);
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
    <SiteActivityWorkExpenditureContext.Provider
      value={{
        selectedExpenditure,
        setSelectedExpenditure,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        globalFilter,
        setGlobalFilter,
        siteId,
        openDrawer,
        closeDrawer,
        openDeleteDialog,
        closeDeleteDialog,
      }}
    >
      {children}
    </SiteActivityWorkExpenditureContext.Provider>
  );
}

export function useSiteActivityWorkExpenditure() {
  const context = useContext(SiteActivityWorkExpenditureContext);
  if (!context) {
    throw new Error(
      'useSiteActivityWorkExpenditure must be used within SiteActivityWorkExpenditureProvider'
    );
  }
  return context;
}
