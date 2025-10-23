import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { useSiteTypeContext } from "../context/site-type-provider";

export function SiteTypePrimaryButtons() {
  const { setShowMutateDrawer, setEditingSiteType } = useSiteTypeContext();

  const handleCreate = () => {
    setEditingSiteType(null);
    setShowMutateDrawer(true);
  };

  return (
    <Button onClick={handleCreate} size="sm">
      <Plus className="mr-2 h-4 w-4" />
      Add Site Type
    </Button>
  );
}
