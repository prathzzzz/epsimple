import React, { createContext, useContext, useState } from 'react';
import type { Vendor } from '@/features/vendors/api/vendors-api';

interface VendorContextType {
  selectedVendor: Vendor | null;
  setSelectedVendor: (vendor: Vendor | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  drawerMode: 'create' | 'edit';
  setDrawerMode: (mode: 'create' | 'edit') => void;
  openCreateDrawer: () => void;
  openEditDrawer: (vendor: Vendor) => void;
  closeDrawer: () => void;
  isBulkUploadDialogOpen: boolean;
  openBulkUploadDialog: () => void;
  closeBulkUploadDialog: () => void;
}

const VendorContext = createContext<VendorContextType | undefined>(undefined);

export const VendorProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [selectedVendor, setSelectedVendor] = useState<Vendor | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [drawerMode, setDrawerMode] = useState<'create' | 'edit'>('create');
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

  const openCreateDrawer = () => {
    setSelectedVendor(null);
    setDrawerMode('create');
    setIsDrawerOpen(true);
  };

  const openEditDrawer = (vendor: Vendor) => {
    setSelectedVendor(vendor);
    setDrawerMode('edit');
    setIsDrawerOpen(true);
  };

  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedVendor(null);
  };

  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true);
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false);

  return (
    <VendorContext.Provider
      value={{
        selectedVendor,
        setSelectedVendor,
        isDrawerOpen,
        setIsDrawerOpen,
        drawerMode,
        setDrawerMode,
        openCreateDrawer,
        openEditDrawer,
        closeDrawer,
        isBulkUploadDialogOpen,
        openBulkUploadDialog,
        closeBulkUploadDialog,
      }}
    >
      {children}
    </VendorContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useVendorContext = () => {
  const context = useContext(VendorContext);
  if (!context) {
    throw new Error('useVendorContext must be used within a VendorProvider');
  }
  return context;
};
