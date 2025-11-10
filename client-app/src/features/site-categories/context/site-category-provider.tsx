import React, { createContext, useContext, useState } from "react";
import type { SiteCategory } from "../api/schema";

interface SiteCategoryContextType {
  editingCategory: SiteCategory | null;
  setEditingCategory: (siteCategory: SiteCategory | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (show: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (show: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const SiteCategoryContext = createContext<SiteCategoryContextType | undefined>(
  undefined
);

export function SiteCategoryProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [editingCategory, setEditingCategory] =
    useState<SiteCategory | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <SiteCategoryContext.Provider
      value={{
        editingCategory,
        setEditingCategory,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </SiteCategoryContext.Provider>
  );
}

export function useSiteCategoryContext() {
  const context = useContext(SiteCategoryContext);
  if (!context) {
    throw new Error(
      "useSiteCategoryContext must be used within SiteCategoryProvider"
    );
  }
  return context;
}
