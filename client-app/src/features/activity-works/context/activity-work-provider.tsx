import React, { createContext, useContext, useState } from 'react';
import type { ActivityWork } from '../api/schema';

interface ActivityWorkContextType {
  selectedActivityWork: ActivityWork | null;
  setSelectedActivityWork: (activityWork: ActivityWork | null) => void;
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

const ActivityWorkContext = createContext<ActivityWorkContextType | undefined>(
  undefined
);

export function ActivityWorkProvider({ children }: { children: React.ReactNode }) {
  const [selectedActivityWork, setSelectedActivityWork] = useState<ActivityWork | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState('');

  const openDrawer = () => setIsDrawerOpen(true);
  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedActivityWork(null);
  };

  const openDeleteDialog = () => setIsDeleteDialogOpen(true);
  const closeDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setSelectedActivityWork(null);
  };

  return (
    <ActivityWorkContext.Provider
      value={{
        selectedActivityWork,
        setSelectedActivityWork,
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
    </ActivityWorkContext.Provider>
  );
}

export function useActivityWork() {
  const context = useContext(ActivityWorkContext);
  if (!context) {
    throw new Error('useActivityWork must be used within ActivityWorkProvider');
  }
  return context;
}
