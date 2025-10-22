import React, { createContext, useContext, useState } from "react";
import type { ActivitiesList } from "../api/schema";

interface ActivitiesListContextType {
  selectedActivitiesList: ActivitiesList | null;
  setSelectedActivitiesList: (activitiesList: ActivitiesList | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
}

const ActivitiesListContext = createContext<ActivitiesListContextType | undefined>(
  undefined
);

export const ActivitiesListProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedActivitiesList, setSelectedActivitiesList] =
    useState<ActivitiesList | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  return (
    <ActivitiesListContext.Provider
      value={{
        selectedActivitiesList,
        setSelectedActivitiesList,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
      }}
    >
      {children}
    </ActivitiesListContext.Provider>
  );
};

export const useActivitiesList = () => {
  const context = useContext(ActivitiesListContext);
  if (!context) {
    throw new Error("useActivitiesList must be used within ActivitiesListProvider");
  }
  return context;
};
