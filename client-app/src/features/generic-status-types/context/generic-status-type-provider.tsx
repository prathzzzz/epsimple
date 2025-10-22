import React, { createContext, useContext, useState } from "react";
import type { GenericStatusType } from "../api/schema";

interface GenericStatusTypeContextType {
  selectedStatusType: GenericStatusType | null;
  setSelectedStatusType: (statusType: GenericStatusType | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
}

const GenericStatusTypeContext = createContext<
  GenericStatusTypeContextType | undefined
>(undefined);

export const GenericStatusTypeProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedStatusType, setSelectedStatusType] =
    useState<GenericStatusType | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  return (
    <GenericStatusTypeContext.Provider
      value={{
        selectedStatusType,
        setSelectedStatusType,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
      }}
    >
      {children}
    </GenericStatusTypeContext.Provider>
  );
};

export const useGenericStatusType = () => {
  const context = useContext(GenericStatusTypeContext);
  if (!context) {
    throw new Error(
      "useGenericStatusType must be used within GenericStatusTypeProvider"
    );
  }
  return context;
};
