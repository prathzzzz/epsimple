import React, { createContext, useContext, useState } from "react";
import type { CostType } from "../api/schema";

interface CostTypesContextType {
  selectedCostType: CostType | null;
  setSelectedCostType: (costType: CostType | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  openBulkUploadDialog: () => void;
  closeBulkUploadDialog: () => void;
}

const CostTypesContext = createContext<CostTypesContextType | undefined>(
  undefined
);

export const CostTypesProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedCostType, setSelectedCostType] = useState<CostType | null>(
    null
  );
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true);
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false);

  return (
    <CostTypesContext.Provider
      value={{
        selectedCostType,
        setSelectedCostType,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
        isBulkUploadDialogOpen,
        openBulkUploadDialog,
        closeBulkUploadDialog,
      }}
    >
      {children}
    </CostTypesContext.Provider>
  );
};

export const useCostTypes = () => {
  const context = useContext(CostTypesContext);
  if (!context) {
    throw new Error("useCostTypes must be used within CostTypesProvider");
  }
  return context;
};
