import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { usePaymentDetails } from "../context/payment-details-provider";

export function PaymentDetailsPrimaryButtons() {
  const { setSelectedPaymentDetails, setIsDrawerOpen, setIsEditMode } =
    usePaymentDetails();

  const handleCreate = () => {
    setSelectedPaymentDetails(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Payment Details
      </Button>
    </div>
  );
}
