import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useSiteCodeGeneratorContext } from "../context/site-code-generator-provider";

export function CreateSiteCodeGeneratorButton() {
  const { setIsDrawerOpen, setEditingGenerator } = useSiteCodeGeneratorContext();

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
