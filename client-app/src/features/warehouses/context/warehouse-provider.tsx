import React, { useState } from 'react';
import { WarehouseContext } from './warehouse-context';
import type { Warehouse } from '../api/schema';

export function WarehouseProvider({ children }: { children: React.ReactNode }) {
  const [selectedWarehouse, setSelectedWarehouse] = useState<Warehouse | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedWarehouse(null);
  };

  const openDeleteDialog = () => setIsDeleteDialogOpen(true);
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setSelectedWarehouse(null);
  };

  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true);
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false);

  return (
    <WarehouseContext.Provider
      value={{
        selectedWarehouse,
        setSelectedWarehouse,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isBulkUploadDialogOpen,
        setIsBulkUploadDialogOpen,
        globalFilter,
        setGlobalFilter,
        openDrawer,
        closeDrawer,
        openDeleteDialog,
        closeDeleteDialog,
        openBulkUploadDialog,
        closeBulkUploadDialog,
      }}
    >
      {children}
    </WarehouseContext.Provider>
  );
}
