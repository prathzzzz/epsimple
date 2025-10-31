import React, { createContext, useContext, useState } from "react";
import type { PaymentDetails } from "../api/schema";

interface PaymentDetailsContextType {
  selectedPaymentDetails: PaymentDetails | null;
  setSelectedPaymentDetails: (paymentDetails: PaymentDetails | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
}

const PaymentDetailsContext = createContext<
  PaymentDetailsContextType | undefined
>(undefined);

export const PaymentDetailsProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedPaymentDetails, setSelectedPaymentDetails] =
    useState<PaymentDetails | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  return (
    <PaymentDetailsContext.Provider
      value={{
        selectedPaymentDetails,
        setSelectedPaymentDetails,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
      }}
    >
      {children}
    </PaymentDetailsContext.Provider>
  );
};

export const usePaymentDetails = () => {
  const context = useContext(PaymentDetailsContext);
  if (!context) {
    throw new Error(
      "usePaymentDetails must be used within PaymentDetailsProvider"
    );
  }
  return context;
};
