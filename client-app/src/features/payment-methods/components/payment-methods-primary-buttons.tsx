import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { usePaymentMethods } from "../context/payment-methods-provider";

export function PaymentMethodsPrimaryButtons() {
  const { setSelectedPaymentMethod, setIsDrawerOpen, setIsEditMode } =
    usePaymentMethods();

  const handleCreate = () => {
    setSelectedPaymentMethod(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Payment Method
      </Button>
    </div>
  );
}
