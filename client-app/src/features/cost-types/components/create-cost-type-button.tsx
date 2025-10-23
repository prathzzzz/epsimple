import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCostTypes } from "../context/cost-types-provider";

export function CreateCostTypeButton() {
  const { setIsDrawerOpen } = useCostTypes();

  return (
    <Button onClick={() => setIsDrawerOpen(true)}>
      <Plus className="mr-2 h-4 w-4" />
      Create Cost Type
    </Button>
  );
}
