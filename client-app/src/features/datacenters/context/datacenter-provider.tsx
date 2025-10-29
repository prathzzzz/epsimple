import React, { createContext, useContext, useState } from 'react';
import type { Datacenter } from '../api/schema';

interface DatacenterContextType {
  selectedDatacenter: Datacenter | null;
  setSelectedDatacenter: (datacenter: Datacenter | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (show: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
  openDrawer: () => void;
  closeDrawer: () => void;
  openDeleteDialog: () => void;
  closeDeleteDialog: () => void;
}

const DatacenterContext = createContext<DatacenterContextType | undefined>(
  undefined
);

export function DatacenterProvider({ children }: { children: React.ReactNode }) {
  const [selectedDatacenter, setSelectedDatacenter] = useState<Datacenter | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
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

  return (
    <DatacenterContext.Provider
      value={{
        selectedDatacenter,
        setSelectedDatacenter,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        globalFilter,
        setGlobalFilter,
        openDrawer,
        closeDrawer,
        openDeleteDialog,
        closeDeleteDialog,
      }}
    >
      {children}
    </DatacenterContext.Provider>
  );
}

export function useDatacenter() {
  const context = useContext(DatacenterContext);
  if (!context) {
    throw new Error('useDatacenter must be used within DatacenterProvider');
  }
  return context;
}
