import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useActivities } from "../context/activities-provider";

export function ActivitiesPrimaryButtons() {
  const { setSelectedActivity, setIsDrawerOpen, setIsEditMode } =
    useActivities();

  const handleCreate = () => {
    setSelectedActivity(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Activity
      </Button>
    </div>
  );
}
