import React, { createContext, useContext, useState } from 'react';
import type { VendorType } from '../api/schema';

interface VendorTypesContextType {
  selectedVendorType: VendorType | null;
  setSelectedVendorType: (vendorType: VendorType | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (isOpen: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (isOpen: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (isOpen: boolean) => void;
  openBulkUploadDialog: () => void;
  closeBulkUploadDialog: () => void;
}

const VendorTypesContext = createContext<VendorTypesContextType | undefined>(undefined);

export const VendorTypesProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [selectedVendorType, setSelectedVendorType] = useState<VendorType | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true);
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false);

  return (
    <VendorTypesContext.Provider
      value={{
        selectedVendorType,
        setSelectedVendorType,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        openBulkUploadDialog,
        closeBulkUploadDialog,
      }}
    >
      {children}
    </VendorTypesContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useVendorTypes = () => {
  const context = useContext(VendorTypesContext);
  if (context === undefined) {
    throw new Error('useVendorTypes must be used within a VendorTypesProvider');
  }
  return context;
};
