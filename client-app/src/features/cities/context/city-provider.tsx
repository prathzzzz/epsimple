import React, { createContext, useContext, useState } from "react";
import type { City } from "../api/schema";

interface CityContextType {
  editingCity: City | null;
  setEditingCity: (city: City | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (show: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (show: boolean) => void;
  globalFilter: string;
  setGlobalFilter: (filter: string) => void;
}

const CityContext = createContext<CityContextType | undefined>(undefined);

export function CityProvider({ children }: { children: React.ReactNode }) {
  const [editingCity, setEditingCity] = useState<City | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [globalFilter, setGlobalFilter] = useState("");

  return (
    <CityContext.Provider
      value={{
        editingCity,
        setEditingCity,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        globalFilter,
        setGlobalFilter,
      }}
    >
      {children}
    </CityContext.Provider>
  );
}

export function useCityContext() {
  const context = useContext(CityContext);
  if (!context) {
    throw new Error("useCityContext must be used within CityProvider");
  }
  return context;
}
