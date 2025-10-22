import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useActivitiesList } from "../context/activities-list-provider";

export function ActivitiesListPrimaryButtons() {
  const { setSelectedActivitiesList, setIsDrawerOpen, setIsEditMode } =
    useActivitiesList();

  const handleCreate = () => {
    setSelectedActivitiesList(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Activities
      </Button>
    </div>
  );
}
