import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useInvoice } from "../context/invoice-provider";

export function InvoicePrimaryButtons() {
  const { setSelectedInvoice, setIsDrawerOpen, setIsEditMode } = useInvoice();

  const handleCreate = () => {
    setSelectedInvoice(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Invoice
      </Button>
    </div>
  );
}
