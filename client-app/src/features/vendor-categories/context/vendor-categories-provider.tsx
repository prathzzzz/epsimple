import React, { createContext, useContext, useState } from 'react';
import type { VendorCategory } from '../api/schema';

interface VendorCategoriesContextType {
  selectedVendorCategory: VendorCategory | null;
  setSelectedVendorCategory: (vendorCategory: VendorCategory | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (isOpen: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (isOpen: boolean) => void;
}

const VendorCategoriesContext = createContext<VendorCategoriesContextType | undefined>(undefined);

export const VendorCategoriesProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [selectedVendorCategory, setSelectedVendorCategory] = useState<VendorCategory | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

  return (
    <VendorCategoriesContext.Provider
      value={{
        selectedVendorCategory,
        setSelectedVendorCategory,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
      }}
    >
      {children}
    </VendorCategoriesContext.Provider>
  );
};

export const useVendorCategories = () => {
  const context = useContext(VendorCategoriesContext);
  if (context === undefined) {
    throw new Error('useVendorCategories must be used within a VendorCategoriesProvider');
  }
  return context;
};
