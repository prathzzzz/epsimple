import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useAssetTagCodeGeneratorContext } from "../context/asset-tag-generator-provider";

export function CreateAssetTagCodeGeneratorButton() {
  const { setIsDrawerOpen, setEditingGenerator } = useAssetTagCodeGeneratorContext();

  const handleClick = () => {
    setEditingGenerator(null);
    setIsDrawerOpen(true);
  };

  return (
    <Button onClick={handleClick} size="sm">
      <Plus className="mr-2 h-4 w-4" />
      Add Generator
    </Button>
  );
}
