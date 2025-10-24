import { createContext, useContext, useState, type ReactNode } from 'react';
import type { PayeeDetails } from '../api/schema';

interface PayeeDetailsContextType {
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  selectedPayeeDetails: PayeeDetails | null;
  setSelectedPayeeDetails: (payeeDetails: PayeeDetails | null) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  payeeDetailsToDelete: PayeeDetails | null;
  setPayeeDetailsToDelete: (payeeDetails: PayeeDetails | null) => void;
  handleEdit: (payeeDetails: PayeeDetails) => void;
  handleDelete: (payeeDetails: PayeeDetails) => void;
  handleCreate: () => void;
  closeDrawer: () => void;
  closeDeleteDialog: () => void;
}

const PayeeDetailsContext = createContext<PayeeDetailsContextType | undefined>(
  undefined
);

export function PayeeDetailsProvider({ children }: { children: ReactNode }) {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [selectedPayeeDetails, setSelectedPayeeDetails] =
    useState<PayeeDetails | null>(null);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [payeeDetailsToDelete, setPayeeDetailsToDelete] =
    useState<PayeeDetails | null>(null);

  const handleEdit = (payeeDetails: PayeeDetails) => {
    setSelectedPayeeDetails(payeeDetails);
    setIsDrawerOpen(true);
  };

  const handleDelete = (payeeDetails: PayeeDetails) => {
    setPayeeDetailsToDelete(payeeDetails);
    setIsDeleteDialogOpen(true);
  };

  const handleCreate = () => {
    setSelectedPayeeDetails(null);
    setIsDrawerOpen(true);
  };

  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedPayeeDetails(null);
  };

  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setPayeeDetailsToDelete(null);
  };

  return (
    <PayeeDetailsContext.Provider
      value={{
        isDrawerOpen,
        setIsDrawerOpen,
        selectedPayeeDetails,
        setSelectedPayeeDetails,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        payeeDetailsToDelete,
        setPayeeDetailsToDelete,
        handleEdit,
        handleDelete,
        handleCreate,
        closeDrawer,
        closeDeleteDialog,
      }}
    >
      {children}
    </PayeeDetailsContext.Provider>
  );
}

export function usePayeeDetails() {
  const context = useContext(PayeeDetailsContext);
  if (context === undefined) {
    throw new Error(
      'usePayeeDetails must be used within a PayeeDetailsProvider'
    );
  }
  return context;
}
