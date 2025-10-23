import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useManagedProjectContext } from "../context/managed-project-provider";

export function CreateManagedProjectButton() {
  const { setIsDrawerOpen } = useManagedProjectContext();

  return (
    <Button onClick={() => setIsDrawerOpen(true)}>
      <Plus className="mr-2 h-4 w-4" />
      Create Managed Project
    </Button>
  );
}
