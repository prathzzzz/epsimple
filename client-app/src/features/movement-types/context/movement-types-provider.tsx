import { createContext, useContext, useState, type ReactNode } from 'react';
import type { MovementType } from '../api/schema';

interface MovementTypesContextType {
  selectedMovementType: MovementType | null;
  setSelectedMovementType: (movementType: MovementType | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (open: boolean) => void;
}

const MovementTypesContext = createContext<MovementTypesContextType | undefined>(undefined);

export function MovementTypesProvider({ children }: { children: ReactNode }) {
  const [selectedMovementType, setSelectedMovementType] = useState<MovementType | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

  return (
    <MovementTypesContext.Provider
      value={{
        selectedMovementType,
        setSelectedMovementType,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
      }}
    >
      {children}
    </MovementTypesContext.Provider>
  );
}

export function useMovementTypes() {
  const context = useContext(MovementTypesContext);
  if (context === undefined) {
    throw new Error('useMovementTypes must be used within a MovementTypesProvider');
  }
  return context;
}
