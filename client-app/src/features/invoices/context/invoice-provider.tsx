import React, { useState } from "react";
import { InvoiceContext } from "./invoice-context";
import type { Invoice } from "../api/schema";

export const InvoiceProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [selectedInvoice, setSelectedInvoice] = useState<Invoice | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);

  return (
    <InvoiceContext.Provider
      value={{
        selectedInvoice,
        setSelectedInvoice,
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
    </InvoiceContext.Provider>
  );
};
