import React, { createContext, useContext, useState } from 'react';
import type { Landlord } from '@/lib/landlords-api';

interface LandlordContextType {
  selectedLandlord: Landlord | null;
  setSelectedLandlord: (landlord: Landlord | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  drawerMode: 'create' | 'edit';
  setDrawerMode: (mode: 'create' | 'edit') => void;
  openCreateDrawer: () => void;
  openEditDrawer: (landlord: Landlord) => void;
  closeDrawer: () => void;
}

const LandlordContext = createContext<LandlordContextType | undefined>(undefined);

export const LandlordProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [selectedLandlord, setSelectedLandlord] = useState<Landlord | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [drawerMode, setDrawerMode] = useState<'create' | 'edit'>('create');

  const openCreateDrawer = () => {
    setSelectedLandlord(null);
    setDrawerMode('create');
    setIsDrawerOpen(true);
  };

  const openEditDrawer = (landlord: Landlord) => {
    setSelectedLandlord(landlord);
    setDrawerMode('edit');
    setIsDrawerOpen(true);
  };

  const closeDrawer = () => {
    setIsDrawerOpen(false);
    setSelectedLandlord(null);
  };

  return (
    <LandlordContext.Provider
      value={{
        selectedLandlord,
        setSelectedLandlord,
        isDrawerOpen,
        setIsDrawerOpen,
        drawerMode,
        setDrawerMode,
        openCreateDrawer,
        openEditDrawer,
        closeDrawer,
      }}
    >
      {children}
    </LandlordContext.Provider>
  );
};

export const useLandlordContext = () => {
  const context = useContext(LandlordContext);
  if (!context) {
    throw new Error('useLandlordContext must be used within a LandlordProvider');
  }
  return context;
};
