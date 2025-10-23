import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useSiteCategoryContext } from "../context/site-category-provider";

export function CreateSiteCategoryButton() {
  const { setIsDrawerOpen } = useSiteCategoryContext();

  return (
    <Button onClick={() => setIsDrawerOpen(true)} size="sm" className="h-8">
      <Plus className="mr-2 h-4 w-4" />
      Create Site Category
    </Button>
  );
}
