import React, { createContext, useContext, useState } from "react";
import type { Site } from "../api/schema";

interface SiteContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  editingSite: Site | null;
  setEditingSite: (site: Site | null) => void;
  deletingSiteId: number | null;
  setDeletingSiteId: (id: number | null) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const SiteContext = createContext<SiteContextType | undefined>(undefined);

export function SiteProvider({ children }: { children: React.ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [editingSite, setEditingSite] = useState<Site | null>(null);
  const [deletingSiteId, setDeletingSiteId] = useState<number | null>(null);
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <SiteContext.Provider
      value={{
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        editingSite,
        setEditingSite,
        deletingSiteId,
        setDeletingSiteId,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </SiteContext.Provider>
  );
}

export function useSiteContext() {
  const context = useContext(SiteContext);
  if (!context) {
    throw new Error("useSiteContext must be used within SiteProvider");
  }
  return context;
}
