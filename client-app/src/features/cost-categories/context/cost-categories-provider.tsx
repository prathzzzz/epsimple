import React, { createContext, useContext, useState } from "react";
import type { CostCategory } from "../api/cost-categories-api";

interface CostCategoriesContextType {
  selectedCostCategory: CostCategory | null;
  setSelectedCostCategory: (costCategory: CostCategory | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
}

const CostCategoriesContext = createContext<
  CostCategoriesContextType | undefined
>(undefined);

export const CostCategoriesProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedCostCategory, setSelectedCostCategory] =
    useState<CostCategory | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  return (
    <CostCategoriesContext.Provider
      value={{
        selectedCostCategory,
        setSelectedCostCategory,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
      }}
    >
      {children}
    </CostCategoriesContext.Provider>
  );
};

export const useCostCategories = () => {
  const context = useContext(CostCategoriesContext);
  if (!context) {
    throw new Error(
      "useCostCategories must be used within CostCategoriesProvider"
    );
  }
  return context;
};
