import React, { createContext, useContext, useState } from "react";
import type { ManagedProject } from "../api/schema";

interface ManagedProjectContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  editingManagedProject: ManagedProject | null;
  setEditingManagedProject: (project: ManagedProject | null) => void;
  deletingManagedProjectId: number | null;
  setDeletingManagedProjectId: (id: number | null) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const ManagedProjectContext = createContext<ManagedProjectContextType | undefined>(undefined);

export function ManagedProjectProvider({ children }: { children: React.ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [editingManagedProject, setEditingManagedProject] = useState<ManagedProject | null>(null);
  const [deletingManagedProjectId, setDeletingManagedProjectId] = useState<number | null>(null);
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <ManagedProjectContext.Provider
      value={{
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        editingManagedProject,
        setEditingManagedProject,
        deletingManagedProjectId,
        setDeletingManagedProjectId,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </ManagedProjectContext.Provider>
  );
}

export function useManagedProjectContext() {
  const context = useContext(ManagedProjectContext);
  if (!context) {
    throw new Error("useManagedProjectContext must be used within ManagedProjectProvider");
  }
  return context;
}
