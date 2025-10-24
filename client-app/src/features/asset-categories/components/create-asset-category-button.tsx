import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { useAssetCategoryContext } from "../context/asset-category-provider";

export function CreateAssetCategoryButton() {
  const { setIsDrawerOpen, setEditingAssetCategory } = useAssetCategoryContext();

  const handleCreate = () => {
    setEditingAssetCategory(null);
    setIsDrawerOpen(true);
  };

  return (
    <Button onClick={handleCreate} size="sm">
      <Plus className="mr-2 h-4 w-4" />
      Create Asset Category
    </Button>
  );
}
