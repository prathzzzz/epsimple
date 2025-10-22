import React, { createContext, useContext, useState } from "react";
import type { Activity } from "../api/schema";

interface ActivitiesContextType {
  selectedActivity: Activity | null;
  setSelectedActivity: (activity: Activity | null) => void;
  isDrawerOpen: boolean;
  setIsDrawerOpen: (open: boolean) => void;
  isDeleteDialogOpen: boolean;
  setIsDeleteDialogOpen: (open: boolean) => void;
  isEditMode: boolean;
  setIsEditMode: (mode: boolean) => void;
}

const ActivitiesContext = createContext<ActivitiesContextType | undefined>(
  undefined
);

export const ActivitiesProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {
  const [selectedActivity, setSelectedActivity] =
    useState<Activity | null>(null);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  return (
    <ActivitiesContext.Provider
      value={{
        selectedActivity,
        setSelectedActivity,
        isDrawerOpen,
        setIsDrawerOpen,
        isDeleteDialogOpen,
        setIsDeleteDialogOpen,
        isEditMode,
        setIsEditMode,
      }}
    >
      {children}
    </ActivitiesContext.Provider>
  );
};

export const useActivities = () => {
  const context = useContext(ActivitiesContext);
  if (!context) {
    throw new Error("useActivities must be used within ActivitiesProvider");
  }
  return context;
};
