import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { usePayeeTypes } from "../context/payee-types-provider";

export function PayeeTypesPrimaryButtons() {
  const { setSelectedPayeeType, setIsDrawerOpen, setIsEditMode } =
    usePayeeTypes();

  const handleCreate = () => {
    setSelectedPayeeType(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Payee Type
      </Button>
    </div>
  );
}
