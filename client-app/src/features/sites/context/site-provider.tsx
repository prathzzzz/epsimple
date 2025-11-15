import React, { useState } from "react";
import { SiteContext } from './site-context';
import type { Site } from '../api/schema';

export function SiteProvider({ children }: { children: React.ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [editingSite, setEditingSite] = useState<Site | null>(null);
  const [deletingSiteId, setDeletingSiteId] = useState<number | null>(null);
  const [globalFilter, setGlobalFilter] = useState("");
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

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
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
      }}
    >
      {children}
    </SiteContext.Provider>
  );
}
