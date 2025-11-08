import React, { createContext, useContext, useState } from "react";
import type { PayeeType } from "../api/schema";

interface PayeeTypesContextType {
  selectedPayeeType: PayeeType | null;
  setSelectedPayeeType: (payeeType: PayeeType | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
  isBulkUploadDialogOpen: boolean;
  setIsBulkUploadDialogOpen: (open: boolean) => void;
}

const PayeeTypesContext = createContext<PayeeTypesContextType | undefined>(
  undefined
);

export const PayeeTypesProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedPayeeType, setSelectedPayeeType] =
    useState<PayeeType | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

  return (
    <PayeeTypesContext.Provider
      value={{
        selectedPayeeType,
        setSelectedPayeeType,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
      }}
    >
      {children}
    </PayeeTypesContext.Provider>
  );
};

export const usePayeeTypes = () => {
  const context = useContext(PayeeTypesContext);
  if (!context) {
    throw new Error("usePayeeTypes must be used within PayeeTypesProvider");
  }
  return context;
};
