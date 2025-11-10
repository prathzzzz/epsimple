import React, { createContext, useContext, useState } from "react";
import type { SiteType } from "../api/schema";

interface SiteTypeContextType {
  editingSiteType: SiteType | null;
  setEditingSiteType: (siteType: SiteType | null) => void;
  showMutateDrawer: boolean;
  setShowMutateDrawer: (show: boolean) => void;
  showDeleteDialog: boolean;
  setShowDeleteDialog: (show: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const SiteTypeContext = createContext<SiteTypeContextType | undefined>(
  undefined
);

export function SiteTypeProvider({ children }: { children: React.ReactNode }) {
  const [editingSiteType, setEditingSiteType] = useState<SiteType | null>(null);
  const [showMutateDrawer, setShowMutateDrawer] = useState(false);
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <SiteTypeContext.Provider
      value={{
        editingSiteType,
        setEditingSiteType,
        showMutateDrawer,
        setShowMutateDrawer,
        showDeleteDialog,
        setShowDeleteDialog,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </SiteTypeContext.Provider>
  );
}

export function useSiteTypeContext() {
  const context = useContext(SiteTypeContext);
  if (!context) {
    throw new Error(
      "useSiteTypeContext must be used within SiteTypeProvider"
    );
  }
  return context;
}
