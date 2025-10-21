import React, { createContext, useContext, useState } from 'react';
import type { AssetType } from '../api/schema';

interface AssetTypesContextType {
  selectedAssetType: AssetType | null;
  setSelectedAssetType: (assetType: AssetType | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (isOpen: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (isOpen: boolean) => void;
}

const AssetTypesContext = createContext<AssetTypesContextType | undefined>(undefined);

export const AssetTypesProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [selectedAssetType, setSelectedAssetType] = useState<AssetType | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);

  return (
    <AssetTypesContext.Provider
      value={{
        selectedAssetType,
        setSelectedAssetType,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
      }}
    >
      {children}
    </AssetTypesContext.Provider>
  );
};

export const useAssetTypes = () => {
  const context = useContext(AssetTypesContext);
  if (context === undefined) {
    throw new Error('useAssetTypes must be used within an AssetTypesProvider');
  }
  return context;
};
