import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCostCategories } from "../context/cost-categories-provider";

export function CostCategoriesPrimaryButtons() {
  const { setSelectedCostCategory, setIsDrawerOpen, setIsEditMode } =
    useCostCategories();

  const handleCreate = () => {
    setSelectedCostCategory(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Cost Category
      </Button>
    </div>
  );
}
