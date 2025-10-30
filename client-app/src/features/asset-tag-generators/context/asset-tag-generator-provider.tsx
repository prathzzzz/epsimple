import { createContext, useContext, useState, type ReactNode } from "react";
import type { AssetTagCodeGenerator } from "../api/schema";

interface AssetTagCodeGeneratorContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  editingGenerator: AssetTagCodeGenerator | null;
  setEditingGenerator: (generator: AssetTagCodeGenerator | null) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  generatorToDelete: AssetTagCodeGenerator | null;
  setGeneratorToDelete: (generator: AssetTagCodeGenerator | null) => void;
}

const AssetTagCodeGeneratorContext = createContext<AssetTagCodeGeneratorContextType | undefined>(undefined);

export function AssetTagCodeGeneratorProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [editingGenerator, setEditingGenerator] = useState<AssetTagCodeGenerator | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [generatorToDelete, setGeneratorToDelete] = useState<AssetTagCodeGenerator | null>(null);

  return (
    <AssetTagCodeGeneratorContext.Provider
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
    </AssetTagCodeGeneratorContext.Provider>
  );
}

export function useAssetTagCodeGeneratorContext() {
  const context = useContext(AssetTagCodeGeneratorContext);
  if (!context) {
    throw new Error("useAssetTagCodeGeneratorContext must be used within a AssetTagCodeGeneratorProvider");
  }
  return context;
}
