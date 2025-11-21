import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { PermissionGuard } from "@/components/permission-guard";
import { useManagedProjectContext } from "../context/managed-project-provider";

export function CreateManagedProjectButton() {
  const { setIsDrawerOpen } = useManagedProjectContext();

  return (
    <PermissionGuard permission="MANAGED_PROJECT:CREATE">
      <Button onClick={() => setIsDrawerOpen(true)}>
        <Plus className="mr-2 h-4 w-4" />
        Create Managed Project
      </Button>
    </PermissionGuard>
  );
}
