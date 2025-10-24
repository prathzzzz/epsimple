import React, { createContext, useContext, useState } from "react";
import type { AssetCategory } from "../api/schema";

interface AssetCategoryContextType {
  editingAssetCategory: AssetCategory | null;
  setEditingAssetCategory: (assetCategory: AssetCategory | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (show: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const AssetCategoryContext = createContext<AssetCategoryContextType | undefined>(
  undefined
);

export function AssetCategoryProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [editingAssetCategory, setEditingAssetCategory] =
    useState<AssetCategory | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <AssetCategoryContext.Provider
      value={{
        editingAssetCategory,
        setEditingAssetCategory,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </AssetCategoryContext.Provider>
  );
}

export function useAssetCategoryContext() {
  const context = useContext(AssetCategoryContext);
  if (!context) {
    throw new Error(
      "useAssetCategoryContext must be used within AssetCategoryProvider"
    );
  }
  return context;
}
