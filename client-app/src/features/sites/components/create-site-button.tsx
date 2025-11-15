import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useSiteContext } from "../context/site-provider";

export function CreateSiteButton() {
  const { setIsDrawerOpen, setEditingSite } = useSite();

  const handleClick = () => {
    setEditingSite(null);
    setIsDrawerOpen(true);
  };

  return (
    <Button onClick={handleClick}>
      <Plus className="mr-2 h-4 w-4" />
      Add Site
    </Button>
  );
}
