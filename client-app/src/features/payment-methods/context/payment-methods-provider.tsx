import React, { createContext, useContext, useState } from "react";
import type { PaymentMethod } from "../api/schema";

interface PaymentMethodsContextType {
  selectedPaymentMethod: PaymentMethod | null;
  setSelectedPaymentMethod: (paymentMethod: PaymentMethod | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
}

const PaymentMethodsContext = createContext<
  PaymentMethodsContextType | undefined
>(undefined);

export const PaymentMethodsProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedPaymentMethod, setSelectedPaymentMethod] =
    useState<PaymentMethod | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  return (
    <PaymentMethodsContext.Provider
      value={{
        selectedPaymentMethod,
        setSelectedPaymentMethod,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
      }}
    >
      {children}
    </PaymentMethodsContext.Provider>
  );
};

export const usePaymentMethods = () => {
  const context = useContext(PaymentMethodsContext);
  if (!context) {
    throw new Error(
      "usePaymentMethods must be used within PaymentMethodsProvider"
    );
  }
  return context;
};
