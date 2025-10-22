import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useGenericStatusType } from "../context/generic-status-type-provider";

export function GenericStatusTypePrimaryButtons() {
  const { setSelectedStatusType, setIsDrawerOpen, setIsEditMode } =
    useGenericStatusType();

  const handleCreate = () => {
    setSelectedStatusType(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Status Type
      </Button>
    </div>
  );
}
