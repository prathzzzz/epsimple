import { createContext, useContext, useState, type ReactNode } from "react";
import type { SiteCodeGenerator } from "../api/schema";

interface SiteCodeGeneratorContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  editingGenerator: SiteCodeGenerator | null;
  setEditingGenerator: (generator: SiteCodeGenerator | null) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  generatorToDelete: SiteCodeGenerator | null;
  setGeneratorToDelete: (generator: SiteCodeGenerator | null) => void;
}

const SiteCodeGeneratorContext = createContext<SiteCodeGeneratorContextType | undefined>(undefined);

export function SiteCodeGeneratorProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingGenerator, setEditingGenerator] = useState<SiteCodeGenerator | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [generatorToDelete, setGeneratorToDelete] = useState<SiteCodeGenerator | null>(null);

  return (
    <SiteCodeGeneratorContext.Provider
      value={{
        isDrawerOpen,
        setIsDrawerOpen,
        editingGenerator,
        setEditingGenerator,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        generatorToDelete,
        setGeneratorToDelete,
      }}
    >
      {children}
    </SiteCodeGeneratorContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useSiteCodeGeneratorContext() {
  const context = useContext(SiteCodeGeneratorContext);
  if (!context) {
    throw new Error("useSiteCodeGeneratorContext must be used within a SiteCodeGeneratorProvider");
  }
  return context;
}
