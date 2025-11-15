import React, { useState } from 'react';
import { DatacenterContext } from './datacenter-context';
import type { Datacenter } from '../api/schema';

export function DatacenterProvider({ children }: { children: React.ReactNode }) {
  const [selectedDatacenter, setSelectedDatacenter] = useState<Datacenter | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isBulkUploadDialogOpen, setIsBulkUploadDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedDatacenter(null);
  };

  const openDeleteDialog = () => setIsDeleteDialogOpen(true);
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setSelectedDatacenter(null);
  };

  const openBulkUploadDialog = () => setIsBulkUploadDialogOpen(true);
  const closeBulkUploadDialog = () => setIsBulkUploadDialogOpen(false);

  return (
    <DatacenterContext.Provider
      value={{
        selectedDatacenter,
        setSelectedDatacenter,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isBulkUploadDialogOpen,
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
    </DatacenterContext.Provider>
  );
}
