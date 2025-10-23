import React, { createContext, useContext, useState } from "react";
import type { PersonDetails } from "../api/schema";

interface PersonDetailsContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  editingPersonDetails: PersonDetails | null;
  setEditingPersonDetails: (personDetails: PersonDetails | null) => void;
  deletingPersonDetailsId: number | null;
  setDeletingPersonDetailsId: (id: number | null) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const PersonDetailsContext = createContext<PersonDetailsContextType | undefined>(
  undefined
);

export function PersonDetailsProvider({ children }: { children: React.ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [editingPersonDetails, setEditingPersonDetails] = useState<PersonDetails | null>(null);
  const [deletingPersonDetailsId, setDeletingPersonDetailsId] = useState<number | null>(null);
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <PersonDetailsContext.Provider
      value={{
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        editingPersonDetails,
        setEditingPersonDetails,
        deletingPersonDetailsId,
        setDeletingPersonDetailsId,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </PersonDetailsContext.Provider>
  );
}

export function usePersonDetailsContext() {
  const context = useContext(PersonDetailsContext);
  if (!context) {
    throw new Error(
      "usePersonDetailsContext must be used within PersonDetailsProvider"
    );
  }
  return context;
}
