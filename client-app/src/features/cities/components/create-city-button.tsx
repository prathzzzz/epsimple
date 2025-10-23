import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCityContext } from "../context/city-provider";

export function CreateCityButton() {
  const { setIsDrawerOpen } = useCityContext();

  return (
    <Button onClick={() => setIsDrawerOpen(true)} size="sm" className="h-8">
      <Plus className="mr-2 h-4 w-4" />
      Create City
    </Button>
  );
}
