import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { usePersonDetailsContext } from "../context/person-details-provider";

export function CreatePersonDetailsButton() {
  const { setIsDrawerOpen, setEditingPersonDetails } = usePersonDetailsContext();

  const handleClick = () => {
    setEditingPersonDetails(null);
    setIsDrawerOpen(true);
  };

  return (
    <Button onClick={handleClick}>
      <Plus className="mr-2 h-4 w-4" />
      Create Person
    </Button>
  );
}
